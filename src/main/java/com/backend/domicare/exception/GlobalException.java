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

import com.backend.domicare.dto.response.RestResponse;

@RestControllerAdvice
public class GlobalException {
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<RestResponse<Object>> handleNotFoundException(NotFoundException e) {
        return buildResponse(HttpStatus.NOT_FOUND, e.getMessage(), "Resource not found");
    }

    @ExceptionHandler(InvalidRefreshToken.class)
    public ResponseEntity<RestResponse<Object>> handleInvalidRefreshToken(InvalidRefreshToken e) {
        return buildResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), "Invalid refresh token");
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<RestResponse<Object>> handleEmailAlreadyExist(EmailAlreadyExistException e) {
        return buildResponse(HttpStatus.CONFLICT, e.getMessage(), "Email already exists");
    }

    @ExceptionHandler(UnconfirmEmailException.class)
    public ResponseEntity<RestResponse<Object>> handleUnconfirmEmail(UnconfirmEmailException e) {
        return buildResponse(HttpStatus.FORBIDDEN, e.getMessage(), "Email not confirmed");
    }

    @ExceptionHandler(DeleteAdminException.class)
    public ResponseEntity<RestResponse<Object>> handleDeleteAdminException(DeleteAdminException e) {
        return buildResponse(HttpStatus.FORBIDDEN, e.getMessage(), "Cannot delete admin");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RestResponse<Object>> handleBadCredentials(BadCredentialsException e) {
        return buildResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), "Lỗi xác thực");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        List<String> errors = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());

        RestResponse<Object> response = new RestResponse<>();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError("Validation Error");
        response.setMessage(errors.size() > 1 ? errors.toString() : errors.get(0));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<Object>> handleGenericException(Exception e) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "An unexpected error occurred");
    }

    private ResponseEntity<RestResponse<Object>> buildResponse(HttpStatus status, String error, String message) {
        RestResponse<Object> response = new RestResponse<>();
        response.setStatus(status.value());
        response.setError(error);
        response.setMessage(message);

        return ResponseEntity.status(status).body(response);
    }
}
