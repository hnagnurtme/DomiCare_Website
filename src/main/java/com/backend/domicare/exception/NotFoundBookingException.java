package com.backend.domicare.exception;

public class NotFoundBookingException extends RuntimeException {
    public NotFoundBookingException(String message) {
        super("Không tìm thấy thông tin cho sản phẩm này" );
    }
}
