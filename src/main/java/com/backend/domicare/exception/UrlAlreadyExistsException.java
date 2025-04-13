package com.backend.domicare.exception;

public class UrlAlreadyExistsException extends RuntimeException {
    public UrlAlreadyExistsException(String message) {
        super("Url đã tồn tại. Vui lòng kiểm tra lại.");
    }
}
