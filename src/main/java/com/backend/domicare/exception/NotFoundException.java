package com.backend.domicare.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super("Không tìm thấy thông tin ");
    }
}
