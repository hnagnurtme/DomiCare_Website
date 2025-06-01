package com.backend.domicare.exception;

public class NotBookedProductException  extends RuntimeException {
    public NotBookedProductException(String message) {
        super("Sản phẩm này chưa được đặt hàng. Vui lòng đặt hàng trước khi đánh giá.");
    }
    
}
