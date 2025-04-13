package com.backend.domicare.exception;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException(String message) {
        super("Không tìm thấy thông tin về người dùng này");
    }
}
