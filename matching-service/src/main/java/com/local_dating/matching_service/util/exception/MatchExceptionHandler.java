package com.local_dating.matching_service.util.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MatchExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(MatchExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        logger.error(e.getClass().getName());
        logger.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
/*
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handleException(AccessDeniedException e) {
        logger.error(e.getClass().getName());
        logger.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity DataIntegrityViolationException(DataIntegrityViolationException e) {
        logger.error(e.getClass().getName() + MessageCode.DATA_INTEGRITY_VIOLATION_EXCEPTION.getMessage());
        logger.error(e.getMessage());
        return new ResponseEntity<>(MessageCode.DATA_INTEGRITY_VIOLATION_EXCEPTION.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity UserAlreadyExistsException(UserAlreadyExistsException e) {
        logger.error(e.getClass().getName() + MessageCode.USER_ALREADY_EXISTS_EXCEPTION.getMessage());
        logger.error(e.getMessage());
        return new ResponseEntity<>(MessageCode.USER_ALREADY_EXISTS_EXCEPTION.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        logger.error(e.getClass().getName() + MessageCode.METHOD_ARGUMENT_NOT_VALID_EXCEPTION.getMessage());
        logger.error(e.getMessage());
        return new ResponseEntity<>(MessageCode.METHOD_ARGUMENT_NOT_VALID_EXCEPTION.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity BadCredentialsException(BadCredentialsException e) {
        logger.error(e.getClass().getName() + MessageCode.BAD_CREDENTIAL_EXCEPTION.getMessage());
        logger.error(e.getMessage());
        return new ResponseEntity<>(MessageCode.BAD_CREDENTIAL_EXCEPTION.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity DataNotFoundException(DataNotFoundException e) {
        logger.error(e.getClass().getName() + MessageCode.DATA_NOT_FOUND_EXCEPTION.getMessage());
        logger.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }*/

    /*@ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Map<String, Object>> handleApplicationException(ApplicationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("errorCode", ex.getErrorCode());
        body.put("message", ex.getMessage());

        HttpStatus status = mapErrorCodeToHttpStatus(ex.getErrorCode());
        return ResponseEntity.status(status).body(body);
    }*/

}
