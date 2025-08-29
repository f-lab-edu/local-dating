package com.local_dating.user_service.application;

import com.local_dating.user_service.config.cache.CacheTtlProperties;
import com.local_dating.user_service.domain.vo.UserVO;
import com.local_dating.user_service.presentation.dto.LoginRes;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserLoginService {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, String> redisTemplate;
    private final CacheTtlProperties cacheTtlProperties;

    @Value("${cache.keys.refreshToken}")
    private String refreshToken;

    private static final Logger logger = LoggerFactory.getLogger(UserLoginService.class);

    public UserLoginService(final JwtUtil jwtUtil,
                            final UserDetailsService userDetailsService,
                            @Qualifier("stringRedisTemplate") final RedisTemplate<String, String> redisTemplate,
                            final CacheTtlProperties cacheTtlProperties) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.redisTemplate = redisTemplate;
        this.cacheTtlProperties = cacheTtlProperties;
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
