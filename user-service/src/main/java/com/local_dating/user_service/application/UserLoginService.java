package com.local_dating.user_service.application;

import com.local_dating.user_service.config.cache.CacheTtlProperties;
import com.local_dating.user_service.domain.vo.UserLoginLogVO;
import com.local_dating.user_service.domain.vo.UserVO;
import com.local_dating.user_service.presentation.dto.LoginRes;
import com.local_dating.user_service.util.HttpServletRequestUtil;
import com.local_dating.user_service.util.JwtUtil;
import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.BusinessException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class UserLoginService {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    //private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, String> redisTemplate;
    private final CacheTtlProperties cacheTtlProperties;
    private final AuthenticationManager authenticationManager;
    private final KafkaProducer kafkaProducer;

    @Value("${cache.keys.refreshToken}")
    private String refreshToken;

    private static final Logger logger = LoggerFactory.getLogger(UserLoginService.class);

    public UserLoginService(final JwtUtil jwtUtil,
                            final CustomUserDetailsService customUserDetailsService,
                            @Qualifier("stringRedisTemplate") final RedisTemplate<String, String> redisTemplate,
                            final CacheTtlProperties cacheTtlProperties,
                            final KafkaProducer kafkaProducer,
                            final AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.redisTemplate = redisTemplate;
        this.cacheTtlProperties = cacheTtlProperties;
        this.kafkaProducer = kafkaProducer;
        this.authenticationManager = authenticationManager;
    }

    public LoginRes login(final UserVO userVO, HttpServletRequest request) { // 컨트롤러 기반 로그인
        //public LoginRes login(final UserDTO userDTO, HttpServletRequest request) {

        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userVO.loginId(), userVO.pwd()));
        final String userId = authentication.getName();logger.info("getname: " + userId);
        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        final UserVO user = new UserVO(userDetails.getUserNo(), userId, userVO.pwd(), userVO.name(), userVO.nickname(), userVO.birth(), userVO.phone());
        final String accessToken = jwtUtil.createAccessToken(user);
        final String refreshToken = jwtUtil.createRefreshToken(user);
        final LoginRes loginRes = new LoginRes(userId, accessToken, refreshToken);

        redisTemplate.opsForValue().set("userRefreshToken:" + userDetails.getUserNo(), refreshToken, cacheTtlProperties.getRefreshTokenTTL(), TimeUnit.DAYS);
        //redisTemplate.opsForValue().set("userRefreshToken:" + userDetails.getId(), refreshToken, 30, TimeUnit.DAYS);
        kafkaProducer.sendMessage("login-log-topic", new UserLoginLogVO(userDetails.getUserNo(), HttpServletRequestUtil.getIpAddress(request), "N", LocalDateTime.now()), false);

        return loginRes;
    }

    public LoginRes refreshTokens(String refreshToken, HttpServletRequest request, long id) {

        String redisRefreshToken = redisTemplate.opsForValue().get("userRefreshToken:" + id);

        if (!refreshToken.equals(redisRefreshToken)) {
            throw new BusinessException(MessageCode.INVALIDATE_REFRESH_TOKEN);
        }

        Claims claims;
        try {
            claims = jwtUtil.parseJwtClaims(refreshToken);
        } catch (Exception e) {
            throw new BusinessException(MessageCode.INVALIDATE_REFRESH_TOKEN);
        }

        String userNo = claims.getSubject();
        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUserNo(Long.parseLong(userNo));
        //CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(loginId);

        UserVO user = new UserVO(
                userDetails.getUserNo(),
                userDetails.getUsername(),
                userDetails.getPassword(), null, null, null, null
        );
        String newAccessToken  = jwtUtil.createAccessToken(user);
        String newRefreshToken = jwtUtil.createRefreshToken(user);

        redisTemplate.opsForValue().set("userRefreshToken:" + id, newRefreshToken, cacheTtlProperties.getRefreshTokenTTL(), TimeUnit.DAYS);

        return new LoginRes(userNo, newAccessToken, newRefreshToken);
    }

}
