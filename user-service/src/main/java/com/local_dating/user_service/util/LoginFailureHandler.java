package com.local_dating.user_service.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.local_dating.user_service.application.KafkaProducer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;
    private final KafkaProducer kafkaProducer;

    @Value("${spring.kafka.topics.login-log}")
    private String loginLogTopic;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String loginId = request.getParameter("loginId");

        //kafkaProducer.sendMessage(loginLogTopic, new UserLoginLogVO(-1L, HttpServletRequestUtil.getIpAddress(request), "Y", LocalDateTime.now()), false);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString("Login Failed: " + exception.getMessage()));
    }
}
