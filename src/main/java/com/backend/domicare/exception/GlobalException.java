package com.backend.domicare.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import  com.backend.domicare.dto.response.RestResponse;

@RestControllerAdvice
public class GlobalException {
    
    @ExceptionHandler(value = {
        NotFoundException.class,
        InvalidRefreshToken.class,
        EmailAlreadyExistException.class,
        UnconfirmEmailException.class,
        BadCredentialsException.class
        })
    public ResponseEntity<RestResponse<Object>> handleException(Exception e) {

        RestResponse<Object> response = new RestResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError(e.getMessage());
        response.setMessage("Failed");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        RestResponse<Object> response = new RestResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError(e.getBody().getDetail());
        List<String> errors = fieldErrors.stream().map(f -> f.getDefaultMessage()).collect(Collectors.toList());
        response.setMessage(errors.size()> 1 ? errors.toString() : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
