package com.backend.domicare.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.backend.domicare.dto.CategoryDTO;
import com.backend.domicare.dto.paging.ResultPagingDTO;
import com.backend.domicare.model.Category;
public interface  CategoryService {
    public CategoryDTO addCategory(CategoryDTO category);
    public void deleteCategory(Long id);
    public CategoryDTO fetchCategoryById(Long categoryId);
    public CategoryDTO updateCategory(Long id, CategoryDTO category);
    public ResultPagingDTO getAllCategories(Specification<Category> spec,Pageable pageable);
}
