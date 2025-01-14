package com.local_dating.user_service.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.local_dating.user_service.domain.vo.UserLoginLogVO;
import com.local_dating.user_service.domain.vo.UserVO;
import com.local_dating.user_service.presentation.dto.LoginRes;
import com.local_dating.user_service.presentation.dto.UserDTO;
import com.local_dating.user_service.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserLoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserLoginService.class);

    public LoginRes login(final UserDTO userDTO, HttpServletRequest request) {

        final Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.loginId(), userDTO.pwd()));
        final String userId = authentication.getName();
        final CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        final UserVO user = new UserVO(userDetails.getId(), userId, userDTO.pwd(), userDTO.name(), userDTO.nickname(), userDTO.birth(), userDTO.phone());
        final String token = jwtUtil.createToken(user);
        final LoginRes loginRes = new LoginRes(userId, token);

        kafkaProducer.sendMessage("login-log-topic", new UserLoginLogVO(userDetails.getId(), request.getRemoteAddr(), "N", LocalDateTime.now()), false);

        return loginRes;
    }
}
