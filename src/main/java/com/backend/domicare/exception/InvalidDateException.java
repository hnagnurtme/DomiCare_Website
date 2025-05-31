package com.backend.domicare.exception;

public class InvalidDateException extends RuntimeException {
    
    public InvalidDateException(String message) {
        super("Ngày tháng năm không hợp lệ");
    }
}
