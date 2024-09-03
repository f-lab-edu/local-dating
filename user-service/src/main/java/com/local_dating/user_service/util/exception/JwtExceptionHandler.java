package com.local_dating.user_service.util.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JwtExceptionHandler {
    @ExceptionHandler(value = {JwtException.class})
    public void handleJwtException(JwtException e) {
        System.out.println(e.getMessage());
    }

    @ExceptionHandler(value = {InvalidateClaimsException.class})
    public void handleInvalidateClaimsException(JwtException e) {
        System.out.println(e.getMessage());
    }
}
