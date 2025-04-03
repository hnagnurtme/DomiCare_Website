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
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundRoleException.class)
    public ResponseEntity<RestResponse<Object>> handleRoleNotFoundException(NotFoundRoleException e) {
        return buildResponse(ExceptionConstants.NOT_FOUND_ROLE, e.getMessage());
    }

    @ExceptionHandler(RoleAlreadyExists.class)
    public ResponseEntity<RestResponse<Object>> handleRoleAlreadyExists(RoleAlreadyExists e) {
        return buildResponse(ExceptionConstants.ROLE_ALREADY_EXISTS, e.getMessage());
    }

    @ExceptionHandler(ProductNotInCategory.class)
    public ResponseEntity<RestResponse<Object>> handleProductNotInCategory(ProductNotInCategory e) {
        return buildResponse(ExceptionConstants.PRODUCT_NOT_IN_CATEGORY, e.getMessage());
    }

    @ExceptionHandler(NotFoundCategoryException.class)
    public ResponseEntity<RestResponse<Object>> handleNotFoundCategoryException(NotFoundCategoryException e) {
        return buildResponse(ExceptionConstants.CATEGORY_NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(NotFoundProductException.class)
    public ResponseEntity<RestResponse<Object>> handleNotFoundProductException(NotFoundProductException e) {
        return buildResponse(ExceptionConstants.NOT_FOUND_PRODUCT_ID, e.getMessage());
    }

    @ExceptionHandler(ProductNameAlreadyExists.class)
    public ResponseEntity<RestResponse<Object>> handleProductNameAlreadyExists(ProductNameAlreadyExists e) {
        return buildResponse(ExceptionConstants.PRODUCT_NAME_ALREADY_EXISTS, e.getMessage());
    }

    @ExceptionHandler(CategoryAlreadyExists.class)
    public ResponseEntity<RestResponse<Object>> handleCategoryAlreadyExists(CategoryAlreadyExists e) {
        return buildResponse(ExceptionConstants.CATEGORY_ALREADY_EXISTS, e.getMessage());
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<RestResponse<Object>> handleCategoryNotFoundException(CategoryNotFoundException e) {
        return buildResponse(ExceptionConstants.CATEGORY_NOT_FOUND, e.getMessage());
    }
    
    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<RestResponse<Object>> handleNotFoundException(NotFoundUserException e) {
        return buildResponse(ExceptionConstants.NOT_FOUND_EMAIL, e.getMessage());
    }

    @ExceptionHandler(InvalidRefreshToken.class)
    public ResponseEntity<RestResponse<Object>> handleInvalidRefreshToken(InvalidRefreshToken e) {
        return buildResponse(ExceptionConstants.INVALID_REFRESH_TOKEN, e.getMessage());
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<RestResponse<Object>> handleEmailAlreadyExist(EmailAlreadyExistException e) {
        return buildResponse(ExceptionConstants.EMAIL_ALREADY_EXISTS, e.getMessage());
    }

    @ExceptionHandler(UnconfirmEmailException.class)
    public ResponseEntity<RestResponse<Object>> handleUnconfirmEmail(UnconfirmEmailException e) {
        return buildResponse(ExceptionConstants.EMAIL_NOT_COMFIRMED, e.getMessage());
    }

    @ExceptionHandler(DeleteAdminException.class)
    public ResponseEntity<RestResponse<Object>> handleDeleteAdminException(DeleteAdminException e) {
        return buildResponse(ExceptionConstants.UNAUTHORIZED_ADMIN_DELETE_OTHER_ADMINS, e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RestResponse<Object>> handleBadCredentials(BadCredentialsException e) {
        return buildResponse(ExceptionConstants.BAD_CREDENTIALS, "Mật khẩu không chính xác");
    }

    @ExceptionHandler(InvalidEmailOrPassword.class)
    public ResponseEntity<RestResponse<Object>> handleInvalidEmailOrPassword(InvalidEmailOrPassword e) {
        return buildResponse(ExceptionConstants.INVALID_EMAIL, e.getMessage());
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
        return buildResponse(ExceptionConstants.INTERNAL_SERVER_ERROR,e.getMessage());
    }

    private ResponseEntity<RestResponse<Object>> buildResponse(ExceptionConstants error, String message) {
        RestResponse<Object> response = new RestResponse<>();
        response.setStatus(error.getCode());
        response.setError(error.getMessageName());
        response.setMessage(message);
        if(
            error == ExceptionConstants.INVALID_EMAIL ||
            error == ExceptionConstants.NOT_FOUND_EMAIL ||
            error == ExceptionConstants.EMAIL_ALREADY_EXISTS ||
            error == ExceptionConstants.BAD_CREDENTIALS ||
            error == ExceptionConstants.INVALID_AUTHENTICATED ||
            error == ExceptionConstants.PRODUCT_NAME_ALREADY_EXISTS ||
            error == ExceptionConstants.NOT_FOUND_PRODUCT_ID ||
            error == ExceptionConstants.CATEGORY_NOT_FOUND ||
            error == ExceptionConstants.CATEGORY_ALREADY_EXISTS ||
            error == ExceptionConstants.PRODUCT_NOT_IN_CATEGORY ||
            error == ExceptionConstants.ROLE_ALREADY_EXISTS ||
            error == ExceptionConstants.NOT_FOUND_ROLE
        )
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        else
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
