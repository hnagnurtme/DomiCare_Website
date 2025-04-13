package com.backend.domicare.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) {
        super("Không tìm thấy danh mục này ");
    }
}
 