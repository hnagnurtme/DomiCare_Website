package com.backend.domicare.exception;

public class EmailSendingException extends RuntimeException {
    public EmailSendingException(String message) {
        super("Email không thể gửi đi");
    }
    
}
