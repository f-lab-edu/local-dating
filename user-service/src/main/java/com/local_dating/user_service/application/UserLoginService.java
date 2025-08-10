package com.local_dating.user_service.application;

import com.local_dating.user_service.domain.vo.UserLoginLogVO;
import com.local_dating.user_service.domain.vo.UserVO;
import com.local_dating.user_service.infrastructure.cache.CacheTtlProperties;
import com.local_dating.user_service.presentation.dto.LoginRes;
import com.local_dating.user_service.util.JwtUtil;
import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.BusinessException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static com.local_dating.user_service.util.HttpServletRequestUtil.getIpAddress;

@Service
public class UserLoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final KafkaProducer kafkaProducer;
    private static final Logger logger = LoggerFactory.getLogger(UserLoginService.class);

    private final UserDetailsService userDetailsService;

    private final RedisTemplate<String, String> redisTemplate;
    private final CacheTtlProperties cacheTtlProperties;

    public UserLoginService(final AuthenticationManager authenticationManager, final JwtUtil jwtUtil,
                            final KafkaProducer kafkaProducer, final UserDetailsService userDetailsService,
                            @Qualifier("stringRedisTemplate") final RedisTemplate<String, String> redisTemplate,
                            final CacheTtlProperties cacheTtlProperties) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.kafkaProducer = kafkaProducer;
        this.userDetailsService = userDetailsService;
        this.redisTemplate = redisTemplate;
        this.cacheTtlProperties = cacheTtlProperties;
    }

    public LoginRes login(final UserVO userVO, HttpServletRequest request) {
        //public LoginRes login(final UserDTO userDTO, HttpServletRequest request) {

        final Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userVO.loginId(), userVO.pwd()));
        final String userId = authentication.getName();logger.info("getname: " + userId);
        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        final UserVO user = new UserVO(userDetails.getId(), userId, userVO.pwd(), userVO.name(), userVO.nickname(), userVO.birth(), userVO.phone());
        final String accessToken = jwtUtil.createAccessToken(user);
        final String refreshToken = jwtUtil.createRefreshToken(user);
        final LoginRes loginRes = new LoginRes(userId, accessToken, refreshToken);

        redisTemplate.opsForValue().set("userRefreshToken:" + userDetails.getId(), refreshToken, cacheTtlProperties.getRefreshTokenTTL(), TimeUnit.DAYS);
        //redisTemplate.opsForValue().set("userRefreshToken:" + userDetails.getId(), refreshToken, 30, TimeUnit.DAYS);
        kafkaProducer.sendMessage("login-log-topic", new UserLoginLogVO(userDetails.getId(), getIpAddress(request), "N", LocalDateTime.now()), false);

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

        String loginId = claims.getSubject();
        CustomUserDetails userDetails =
                (CustomUserDetails) userDetailsService.loadUserByUsername(loginId);

        UserVO user = new UserVO(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getPassword(), null, null, null, null
        );
        String newAccessToken  = jwtUtil.createAccessToken(user);
        String newRefreshToken = jwtUtil.createRefreshToken(user);

        redisTemplate.opsForValue().set("userRefreshToken:" + id, newRefreshToken, cacheTtlProperties.getRefreshTokenTTL(), TimeUnit.DAYS);

        return new LoginRes(loginId, newAccessToken, newRefreshToken);
    }

}
