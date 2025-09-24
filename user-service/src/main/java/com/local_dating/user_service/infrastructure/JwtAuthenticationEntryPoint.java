package com.local_dating.user_service.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.local_dating.user_service.util.MessageCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> body = new HashMap<>();

        String code = MessageCode.INSUFFICIENT_AUTHENTICATION.getCode();
        String message = MessageCode.INSUFFICIENT_AUTHENTICATION.getMessage();

        if (ex instanceof BadCredentialsException) {
            code = MessageCode.BAD_CREDENTIAL_EXCEPTION.getCode();
            message = MessageCode.BAD_CREDENTIAL_EXCEPTION.getMessage();
        }
        log.error(ex.getMessage());
        body.put("code", code);
        body.put("message", message);

        mapper.writeValue(response.getWriter(), body);

    }
}