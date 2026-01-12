package com.local_dating.user_service.application;

import com.local_dating.user_service.config.cache.CacheTtlProperties;
import com.local_dating.user_service.domain.entity.User;
import com.local_dating.user_service.domain.vo.UserLoginLogVO;
import com.local_dating.user_service.domain.vo.UserVO;
import com.local_dating.user_service.infrastructure.repository.UserRepository;
import com.local_dating.user_service.presentation.dto.LoginRes;
import com.local_dating.user_service.util.HttpServletRequestUtil;
import com.local_dating.user_service.util.JwtUtil;
import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class UserInterceptorLoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final CacheTtlProperties cacheTtlProperties;
    private final KafkaProducer kafkaProducer;

    public UserInterceptorLoginService(
            final UserRepository userRepository,
            final PasswordEncoder passwordEncoder,
            final JwtUtil jwtUtil,
            @Qualifier("stringRedisTemplate") final RedisTemplate<String, String> redisTemplate,
            final CacheTtlProperties cacheTtlProperties,
            final KafkaProducer kafkaProducer) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
        this.cacheTtlProperties = cacheTtlProperties;
        this.kafkaProducer = kafkaProducer;
    }

    @Transactional
    public LoginRes login(final UserVO userVO, HttpServletRequest request) {

        User user = userRepository.findByLoginId(userVO.loginId())
                .orElseThrow(() -> {
                    throw new BusinessException(MessageCode.USER_NOT_FOUND);
                });

        if (!passwordEncoder.matches(userVO.pwd(), user.getPwd())) {
            throw new BusinessException(MessageCode.USER_NOT_FOUND);
        }

        UserVO newVO = new UserVO(
                user.getNo(),
                user.getLoginId(),
                null,
                user.getName(),
                user.getNickname(),
                user.getBirth(),
                user.getPhone()
        );

        String accessToken = jwtUtil.createAccessToken(newVO);
        String refreshToken = jwtUtil.createRefreshToken(newVO);

        String key = "userRefreshToken:" + user.getNo();
        redisTemplate.opsForValue().set(
                key,
                refreshToken,
                cacheTtlProperties.getRefreshTokenTTL(),
                TimeUnit.DAYS
        );

        kafkaProducer.sendMessage(
                "login-log-topic",
                new UserLoginLogVO(
                        user.getNo(),
                        HttpServletRequestUtil.getIpAddress(request),
                        "N",
                        LocalDateTime.now()
                ),
                false
        );

        user.setLastLoginDate(LocalDateTime.now());

        return new LoginRes(userVO.loginId(), accessToken, refreshToken);
    }
}
