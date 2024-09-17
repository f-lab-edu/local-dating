package com.local_dating.user_service.util.exception;

public class DataAlreadyExistsException extends RuntimeException {
    public DataAlreadyExistsException(final String message) {
        super(message);
    }
}
