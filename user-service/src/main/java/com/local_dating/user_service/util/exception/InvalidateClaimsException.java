package com.local_dating.user_service.util.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidateClaimsException extends AuthenticationException {
    public InvalidateClaimsException(String msg) {
        super(msg);
    }
}