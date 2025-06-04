package com.backend.domicare.exception;

public class AlreadyPendingBooking extends RuntimeException {
    public AlreadyPendingBooking(String message) {
        super("Bạn đã có một đơn hàng đang chờ xử lý. Vui lòng đợi hoặc hủy đơn hàng trước khi tạo đơn hàng mới.");
    }
}
