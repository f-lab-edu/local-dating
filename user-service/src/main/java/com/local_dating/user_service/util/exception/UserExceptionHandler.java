package com.local_dating.user_service.util.exception;

import com.local_dating.user_service.presentation.controller.UserController;
import com.local_dating.user_service.util.MessageCode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {UserController.class})
public class UserExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        System.out.println(e.getClass().getName());
        System.out.println(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        //return new ResponseEntity<>("registerFail", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity DataIntegrityViolationException(DataIntegrityViolationException e) {
        System.out.println(e.getClass().getName() + MessageCode.DATA_INTEGRITY_VIOLATION_EXCEPTION.getMessage());
        System.out.println(e.getMessage());
        return new ResponseEntity<>(MessageCode.DATA_INTEGRITY_VIOLATION_EXCEPTION.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity UserAlreadyExistsException(UserAlreadyExistsException e) {
        System.out.println(e.getClass().getName() + MessageCode.USER_ALREADY_EXISTS_EXCEPTION.getMessage());
        System.out.println(e.getMessage());
        return new ResponseEntity<>(MessageCode.USER_ALREADY_EXISTS_EXCEPTION.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        System.out.println(e.getClass().getName() + MessageCode.METHOD_ARGUMENT_NOT_VALID_EXCEPTION.getMessage());
        System.out.println(e.getMessage());
        return new ResponseEntity<>(MessageCode.METHOD_ARGUMENT_NOT_VALID_EXCEPTION.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity BadCredentialsException(BadCredentialsException e) {
        System.out.println(e.getClass().getName() + MessageCode.BAD_CREDENTIAL_EXCEPTION.getMessage());
        System.out.println(e.getMessage());
        return new ResponseEntity<>(MessageCode.BAD_CREDENTIAL_EXCEPTION.getMessage(), HttpStatus.BAD_REQUEST);
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
