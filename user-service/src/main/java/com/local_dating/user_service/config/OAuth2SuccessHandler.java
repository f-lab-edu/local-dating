package com.local_dating.user_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.local_dating.user_service.application.KafkaProducer;
import com.local_dating.user_service.application.UserCoinService;
import com.local_dating.user_service.config.cache.CacheTtlProperties;
import com.local_dating.user_service.domain.entity.User;
import com.local_dating.user_service.domain.type.RoleType;
import com.local_dating.user_service.domain.vo.UserLoginLogVO;
import com.local_dating.user_service.domain.vo.UserVO;
import com.local_dating.user_service.infrastructure.repository.UserRepository;
import com.local_dating.user_service.presentation.dto.LoginRes;
import com.local_dating.user_service.util.HttpServletRequestUtil;
import com.local_dating.user_service.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
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

        final OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        log.info("Google OAuth2 authentication, attributeKeys={}", oauthUser.getAttributes().keySet());
        final User user = findOrCreateGoogleUser(oauthUser);

        user.setLastLoginDate(LocalDateTime.now());

        final UserVO tokenUser = new UserVO(
                user.getNo(),
                user.getLoginId(),
                user.getPwd(),
                user.getName(),
                user.getNickname(),
                user.getBirth(),
                user.getPhone()
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

    private User findOrCreateGoogleUser(final OAuth2User oauthUser) {
        final Map<String, Object> attributes = oauthUser.getAttributes();
        final String googleSubject = (String) attributes.get("sub"); // 구글 식별자
        final String email = (String) attributes.get("email");
        final String name = (String) attributes.get("name");
        final String loginId = "google:" + googleSubject;

        return userRepository.findByLoginId(loginId)
                .or(() -> findByEmailGoogle(email))
                .orElseGet(() -> createGoogleUser(loginId, email, name));
    }

    private Optional<User> findByEmailGoogle(final String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }

        return userRepository.findByEmail(email)
                .filter(user -> user.getLoginId() != null && user.getLoginId().startsWith("google:"));
    }

    private User createGoogleUser(final String loginId, final String email, final String name) {
        final User user = new User();
        user.setLoginId(loginId);
        user.setEmail(email);
        user.setName(name);
        user.setNickname(name);
        user.setRole(RoleType.USER);
        user.setStatusCd("ACTIVE");
        user.setLgFail(0L);

        final User saved = userRepository.save(user);
        userCoinService.saveNewCoinData(saved.getNo());
        return saved;
    }

}
