package com.backend.domicare.service;


public interface  CategoryService {
    public void addCategory(String name);
    public void deleteCategory(Long id);
    public void updateCategory(Long id, String name);
    public void addProductToCategory(Long categoryId, Long productId);
    public void removeProductFromCategory(Long categoryId, Long productId);
}
