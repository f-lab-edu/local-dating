package com.local_dating.user_service.application;

import com.local_dating.user_service.domain.vo.UserLoginLogVO;
import com.local_dating.user_service.domain.vo.UserVO;
import com.local_dating.user_service.presentation.dto.LoginRes;
import com.local_dating.user_service.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserLoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final KafkaProducer kafkaProducer;
    private static final Logger logger = LoggerFactory.getLogger(UserLoginService.class);

    private final UserDetailsService userDetailsService;

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

        kafkaProducer.sendMessage("login-log-topic", new UserLoginLogVO(userDetails.getId(), request.getRemoteAddr(), "N", LocalDateTime.now()), false);

        return loginRes;
    }

    public LoginRes refreshTokens(String refreshToken, HttpServletRequest request) {
        Claims claims = jwtUtil.parseJwtClaims(refreshToken);
        /*if (jwtUtil.resolveClaims(request)) {
            throw new TokenExpiredException("토큰만료");
        }*/

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

        return new LoginRes(loginId, newAccessToken, newRefreshToken);
    }

}
