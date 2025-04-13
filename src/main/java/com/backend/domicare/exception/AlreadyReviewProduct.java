package com.backend.domicare.exception;

public class AlreadyReviewProduct extends RuntimeException {
    public AlreadyReviewProduct(String message) {
        super("Bạn đã đánh giá sản phẩm này rồi ");
    }
}
