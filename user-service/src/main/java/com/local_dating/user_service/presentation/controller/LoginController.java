package com.local_dating.user_service.presentation.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.local_dating.user_service.application.KafkaProducer;
import com.local_dating.user_service.domain.vo.UserLoginLogVO;
import com.local_dating.user_service.domain.vo.UserVO;
import com.local_dating.user_service.presentation.dto.LoginRes;
import com.local_dating.user_service.presentation.dto.UserDTO;
import com.local_dating.user_service.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/v1/users/login")
    public LoginRes login(@RequestBody @Valid final UserDTO userDTO, HttpServletRequest request) {

        final Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.loginId(), userDTO.pwd()));
        final String userId = authentication.getName();
        final UserVO user = new UserVO(Long.parseLong(userId), userDTO.pwd(), userDTO.name(), userDTO.nickname(), userDTO.birth(), userDTO.phone());
        final String token = jwtUtil.createToken(user);
        final LoginRes loginRes = new LoginRes(userId, token);

        try {
            kafkaProducer.sentLoginLog("my-topic", objectMapper.writeValueAsString(new UserLoginLogVO(userId, request.getRemoteAddr(), "N", LocalDateTime.now())));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return loginRes;
    }
}
