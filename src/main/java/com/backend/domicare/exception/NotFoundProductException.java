package com.backend.domicare.exception;

public class NotFoundProductException extends RuntimeException {
    public NotFoundProductException(String message) {
        super("Không tìm thấy thông tin về sản phẩm này");
    }
}
