package com.backend.domicare.exception;

public class CategoryAlreadyExists extends RuntimeException {
    public CategoryAlreadyExists(String message) {
        super("Đã tồn tại danh mục với tên này. Vui lòng chọn tên khác.");
    }
}
