package com.local_dating.user_service.util.exception;

import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JwtExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(JwtExceptionHandler.class);

    @ExceptionHandler(value = {JwtException.class})
    public void handleJwtException(JwtException e) {
        logger.error(e.getMessage());
    }

    @ExceptionHandler(value = {InvalidateClaimsException.class})
    public void handleInvalidateClaimsException(JwtException e) {
        logger.error(e.getMessage());
    }
}
