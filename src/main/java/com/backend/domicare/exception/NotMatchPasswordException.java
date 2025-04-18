package com.backend.domicare.exception;

public class NotMatchPasswordException extends RuntimeException {
    public NotMatchPasswordException(String message) {
        super("Mật khẩu không khớp");
    }
}
