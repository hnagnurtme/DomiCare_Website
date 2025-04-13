package com.backend.domicare.exception;

public class BookingStatusException extends RuntimeException {
    public BookingStatusException(String message) {
        super("Trạng thái đặt lịch không hợp lệ ");
    }
}
