package com.local_dating.user_service.util.exception;

import com.local_dating.user_service.util.MessageCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
//@RestControllerAdvice(assignableTypes = {UserController.class})
public class UserExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserExceptionHandler.class);

    @ExceptionHandler(Exception.class) // 별도 핸들러에 지정되지 않은 모든 예외가 잡힘
    public ResponseEntity handleException(Exception e) {
        logger.error(e.getClass().getName());
        logger.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        //return new ResponseEntity<>("registerFail", HttpStatus.INTERNAL_SERVER_ERROR);
    }

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
    }

    /*@ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Map<String, Object>> handleApplicationException(ApplicationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("errorCode", ex.getErrorCode());
        body.put("message", ex.getMessage());

        HttpStatus status = mapErrorCodeToHttpStatus(ex.getErrorCode());
        return ResponseEntity.status(status).body(body);
    }*/

}
