package com.local_dating.user_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.local_dating.user_service.application.KafkaProducer;
import com.local_dating.user_service.application.UserCoinService;
import com.local_dating.user_service.config.cache.CacheTtlProperties;
import com.local_dating.user_service.domain.entity.OAuthInfo;
import com.local_dating.user_service.domain.entity.User;
import com.local_dating.user_service.domain.type.RoleType;
import com.local_dating.user_service.domain.vo.UserLoginLogVO;
import com.local_dating.user_service.domain.vo.UserOAuthLinkVO;
import com.local_dating.user_service.domain.vo.UserVO;
import com.local_dating.user_service.infrastructure.repository.OAuthInfoRepository;
import com.local_dating.user_service.infrastructure.repository.UserRepository;
import com.local_dating.user_service.presentation.dto.LoginRes;
import com.local_dating.user_service.util.HttpServletRequestUtil;
import com.local_dating.user_service.util.JwtUtil;
import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.BusinessException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final OAuthInfoRepository oAuthInfoRepository;
    private final UserCoinService userCoinService;
    private final CacheTtlProperties cacheTtlProperties;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    @Qualifier("stringRedisTemplate")
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${spring.kafka.topics.login-log}")
    private String loginLogTopic;

    @Override
    @Transactional
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {

        final OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        final String provider = oauthToken.getAuthorizedClientRegistrationId(); // google 등 oauth 제공자 확인

        final OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        log.info("Google OAuth2 authentication, attributeKeys={}", oauthUser.getAttributes().keySet());

        // 기존 일반 가입자 OAuth 정보 연동
        UserOAuthLinkVO userOAuthLinkVO;
        final HttpSession session = request.getSession(false); // 신규 세션생성x
        if (session != null) {
            userOAuthLinkVO = (UserOAuthLinkVO) session.getAttribute("OAUTH_LINK_INFO");
            session.removeAttribute("OAUTH_LINK_INFO");

            if (userOAuthLinkVO != null) {
                linkOauth(userOAuthLinkVO, provider, oauthUser);
                return;
            }
        }


        final User user = checkProvider(provider, oauthUser);

        user.setLastLoginDate(LocalDateTime.now());

        final UserVO tokenUser = new UserVO(
                user.getNo(),
                user.getLoginId(),
                user.getPwd(),
                user.getName(),
                user.getNickname(),
                user.getBirth(),
                user.getPhone(),
                user.getEmail()
        );

        final String accessToken = jwtUtil.createAccessToken(tokenUser);
        final String refreshToken = jwtUtil.createRefreshToken(tokenUser);

        redisTemplate.opsForValue().set(
                "userRefreshToken:" + user.getNo(),
                refreshToken,
                cacheTtlProperties.getRefreshTokenTTL(),
                TimeUnit.DAYS
        );

        kafkaProducer.sendMessage(
                loginLogTopic,
                new UserLoginLogVO(
                        user.getNo(),
                        HttpServletRequestUtil.getIpAddress(request),
                        "Y",
                        LocalDateTime.now()
                ),
                false
        );

        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("Refresh-Token", refreshToken);

        objectMapper.writeValue(
                response.getWriter(),
                new LoginRes(String.valueOf(user.getNo()), accessToken, refreshToken)
        );
    }

    private void linkOauth(final UserOAuthLinkVO userOAuthLinkVO, final String provider, final OAuth2User oauthUser) {

        userRepository.findById(userOAuthLinkVO.userNo())
                .map(el -> {
                    final Map<String, Object> attributes = oauthUser.getAttributes();
                    final String subjectId = (String) attributes.get("sub");
                    return this.createOauthInfo(provider, subjectId, el.getEmail(), el.getNo());
                    //return this.createOauthInfo(provider + ":" + subjectId, el.getEmail(), el.getNo());
                })
                .orElseThrow(() -> new BusinessException(MessageCode.USER_NOT_FOUND));
    }

    private User checkProvider(String provider, OAuth2User oauthUser) {

        switch (provider) {
            case "google":
                return findOrCreateGoogleUser(oauthUser);
            default:
                throw new BusinessException(MessageCode.PROVIDER_NOT_FOUND);
        }
    }

    private User findOrCreateGoogleUser(final OAuth2User oauthUser) {
        final Map<String, Object> attributes = oauthUser.getAttributes();
        final String googleSubject = (String) attributes.get("sub"); // 구글 식별자
        final String email = (String) attributes.get("email");
        final String name = (String) attributes.get("name");
        final String providerId = "google:" + googleSubject;

        return oAuthInfoRepository.findByProviderId(providerId)
                .or(() -> findByEmailGoogle(email))
                .map(el->userRepository.findById(el.getUserNo())
                        .orElseThrow(() -> new BusinessException(MessageCode.USER_NOT_FOUND)))
                .orElseGet(() -> {
                            userRepository.findByEmail(email).map(el -> createOauthInfo("google", googleSubject, email, el.getNo()))
                            //userRepository.findByEmail(email).map(el -> createOauthInfo(providerId, email, el.getNo()))
                                    .orElseGet(() -> createGoogleUser(providerId, email, name));
                            return null;
                        }
                ); // oauth 테이블에 데이터가 없으면 생성
                //.orElseGet(() -> createGoogleUser(providerId, email, name)); // oauth 테이블에 데이터가 없으면 생성

        /*return userRepository.findByLoginId(loginId)
                .or(() -> findByEmailGoogle(email))
                .orElseGet(() -> createGoogleUser(loginId, email, name));*/
    }

    private Optional<OAuthInfo> findByEmailGoogle(final String email) {
    //private Optional<User> findByEmailGoogle(final String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }

        return oAuthInfoRepository.findByEmail(email)
                .filter(user -> user.getProviderId() != null && user.getProviderId().startsWith("google:"));

        /*return userRepository.findByEmail(email)
                .filter(user -> user.getLoginId() != null && user.getLoginId().startsWith("google:"));*/
    }

    private User createGoogleUser(final String providerId, final String email, final String name) { // user 테이블, oauth 테이블 저장
        final User user = new User();
        //user.setLoginId(loginId);
        user.setEmail(email);
        user.setName(name);
        user.setNickname(name);
        user.setRole(RoleType.USER);
        user.setStatusCd("ACTIVE");
        user.setLgFail(0L);
        final User saved = userRepository.save(user);

        final OAuthInfo oAuthInfo = new OAuthInfo();
        oAuthInfo.setUserNo(saved.getNo());
        oAuthInfo.setProvider("google");
        oAuthInfo.setProviderId(providerId); //"google:" + googleSubject 형태
        oAuthInfo.setEmail(email);
        oAuthInfoRepository.save(oAuthInfo);

        userCoinService.saveNewCoinData(saved.getNo());
        return saved;
    }

    private User createOauthInfo(final String provider, final String subjectId, final String email, final Long userNo) { // oauth 테이블만 저장
    //private User createOauthInfo(final String providerId, final String email, final Long userNo) { // oauth 테이블만 저장

        final OAuthInfo oAuthInfo = new OAuthInfo();
        oAuthInfo.setUserNo(userNo);
        oAuthInfo.setProvider(provider);
        oAuthInfo.setProviderId(provider + ":" + subjectId); //"google:" + googleSubject 형태
        /*oAuthInfo.setProvider("google");
        oAuthInfo.setProviderId(providerId); //"google:" + googleSubject 형태*/
        oAuthInfo.setEmail(email);
        oAuthInfoRepository.save(oAuthInfo);

        return null;

    }

}
