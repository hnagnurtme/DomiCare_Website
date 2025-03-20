package com.backend.domicare.service.imp;

import org.springframework.stereotype.Service;

import com.backend.domicare.dto.CategoryDTO;
import com.backend.domicare.exception.NotFoundException;
import com.backend.domicare.mapper.CategoryMapper;
import com.backend.domicare.model.Category;
import com.backend.domicare.repository.CategoriesRepository;
import com.backend.domicare.repository.ProductsRepository;
import com.backend.domicare.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements  CategoryService {
    private final CategoriesRepository categoryRepository;
    private final ProductsRepository productRepository;

    @Override
    public void addProductToCategory(Long categoryId, Long productId) {
        boolean isExist = categoryRepository.existsById(categoryId);
        if (!isExist) {
            throw new NotFoundException("Category not found");
        }
        Category category = categoryRepository.findById(categoryId).get();
        boolean isProductExist = productRepository.existsById(productId);
        if (!isProductExist) {
            throw new NotFoundException("Product not found");
        }
        category.getProducts().add(productRepository.findById(productId).get());
        categoryRepository.save(category);
    }

    @Override
    public void removeProductFromCategory(Long categoryId, Long productId) {
        boolean isExist = categoryRepository.existsById(categoryId);
        if (!isExist) {
            throw new NotFoundException("Category not found");
        }
        Category category = categoryRepository.findById(categoryId).get();
        boolean isProductExist = productRepository.existsById(productId);
        if (!isProductExist) {
            throw new NotFoundException("Product not found");
        }
        category.getProducts().remove(productRepository.findById(productId).get());
        categoryRepository.save(category);
    }
    
    @Override
    public Category fetchCategoryById(Long categoryId){
        boolean isExist = categoryRepository.existsById(categoryId);
        if (!isExist) {
            throw new NotFoundException("Category not found");
        }
        return categoryRepository.findById(categoryId).get();
    }

    @Override
    public CategoryDTO addCategory(CategoryDTO category) {
        Category categoryEntity = CategoryMapper.INSTANCE.convertToCategory(category);
        if (categoryEntity == null) {
            throw  new NotFoundException("Category not found");
        }
        categoryRepository.save(categoryEntity);
        return CategoryMapper.INSTANCE.convertToCategoryDTO(categoryEntity);
    }

    @Override
    public void deleteCategory(Long id) {
        boolean isExist = categoryRepository.existsById(id);
        if (!isExist) {
            throw new NotFoundException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO category) {
        boolean isExist = categoryRepository.existsById(id);
        if (!isExist) {
            throw new NotFoundException("Category not found");
        }
        Category categoryEntity = CategoryMapper.INSTANCE.convertToCategory(category);
        categoryEntity.setId(id);
        categoryRepository.save(categoryEntity);
        return CategoryMapper.INSTANCE.convertToCategoryDTO(categoryEntity);
    }
}
