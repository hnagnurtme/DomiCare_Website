package com.backend.domicare.exception;

public class NotFoundCategoryException extends RuntimeException {
    public NotFoundCategoryException(String message) {
        super("Không tìm thấy thông tin cho danh mục này" );
    }
    
}
