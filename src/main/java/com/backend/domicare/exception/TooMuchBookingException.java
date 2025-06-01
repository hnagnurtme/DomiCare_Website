package com.backend.domicare.exception;

public class TooMuchBookingException extends RuntimeException {
    public TooMuchBookingException(String message) {
        super("Bạn đã đặt quá 5 đơn hàng trong 1 giờ qua. Vui lòng thử lại sau.");
    }
    
}
