package com.local_dating.user_service.util;

import com.local_dating.user_service.application.CustomUserDetails;
import com.local_dating.user_service.application.KafkaProducer;
import com.local_dating.user_service.config.cache.CacheTtlProperties;
import com.local_dating.user_service.domain.vo.UserLoginLogVO;
import com.local_dating.user_service.domain.vo.UserVO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
//@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final KafkaProducer kafkaProducer;
    private final RedisTemplate<String, String> redisTemplate;
    private final CacheTtlProperties cacheTtlProperties;

    @Value("${spring.kafka.topics.login-log}")
    private String loginLogTopic;

    public LoginSuccessHandler(JwtUtil jwtUtil, KafkaProducer kafkaProducer, @Qualifier("stringRedisTemplate") final RedisTemplate<String, String> redisTemplate, CacheTtlProperties cacheTtlProperties) {
        this.jwtUtil = jwtUtil;
        this.kafkaProducer = kafkaProducer;
        this.redisTemplate = redisTemplate;
        this.cacheTtlProperties = cacheTtlProperties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        UserVO user = new UserVO(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getPassword(),
                null, null, null, null
        );

        String accessToken = jwtUtil.createAccessToken(user);
        String refreshToken = jwtUtil.createRefreshToken(user);

        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("Refresh-Token", refreshToken);

        redisTemplate.opsForValue().set(
                "userRefreshToken:" + userDetails.getId(),
                refreshToken,
                cacheTtlProperties.getRefreshTokenTTL(),
                TimeUnit.DAYS
        );

        kafkaProducer.sendMessage(
                loginLogTopic,
                new UserLoginLogVO(
                        userDetails.getId(),
                        HttpServletRequestUtil.getIpAddress(request),
                        "N",
                        LocalDateTime.now()
                ),
                false
        );

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
