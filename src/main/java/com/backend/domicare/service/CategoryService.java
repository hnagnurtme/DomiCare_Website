package com.backend.domicare.service;

import com.backend.domicare.dto.CategoryDTO;
import com.backend.domicare.model.Category;

public interface  CategoryService {
    public CategoryDTO addCategory(CategoryDTO category);
    public void deleteCategory(Long id);
    public CategoryDTO updateCategory(Long id, CategoryDTO category);
    public void addProductToCategory(Long categoryId, Long productId);
    public void removeProductFromCategory(Long categoryId, Long productId);
    public Category fetchCategoryById(Long categoryId);
}
