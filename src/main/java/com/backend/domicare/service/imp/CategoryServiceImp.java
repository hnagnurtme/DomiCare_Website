package com.backend.domicare.service.imp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.domicare.dto.CategoryDTO;
import com.backend.domicare.dto.paging.ResultPagingDTO;
import com.backend.domicare.exception.NotFoundException;
import com.backend.domicare.mapper.CategoryMapper;
import com.backend.domicare.model.Category;
import com.backend.domicare.repository.CategoriesRepository;
import com.backend.domicare.repository.ProductsRepository;
import com.backend.domicare.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImp implements CategoryService {

    private final CategoriesRepository categoryRepository;
    private final ProductsRepository productRepository;

    // Fetch category by ID
    @Override
    public CategoryDTO fetchCategoryById(Long categoryId) {
        // Retrieve category, throw exception if not found
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new NotFoundException("Category not found"));
        return CategoryMapper.INSTANCE.convertToCategoryDTO(category);
    }

    // Add a new category
    @Override
    public CategoryDTO addCategory(CategoryDTO categoryDTO) {
        // Convert DTO to entity and save
        Category categoryEntity = CategoryMapper.INSTANCE.convertToCategory(categoryDTO);
        categoryRepository.save(categoryEntity);
        return CategoryMapper.INSTANCE.convertToCategoryDTO(categoryEntity);
    }

    // Delete category and its associated products
    @Transactional
    @Override
    public void deleteCategory(Long id) {
        // Check if category exists
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Category not found"));

        // If category has products, delete them in batch
        if (!category.getProducts().isEmpty()) {
            productRepository.deleteByCategoryId(id);  // Deletes all products by category ID
        }

        // Delete the category
        categoryRepository.deleteById(id);
    }

    // Update category information
    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        // Find existing category
        Category existingCategory = categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Category not found"));

        // Update fields with values from DTO
        existingCategory.setName(categoryDTO.getName());
        existingCategory.setDescription(categoryDTO.getDescription());

        // Return updated category as DTO
        return CategoryMapper.INSTANCE.convertToCategoryDTO(existingCategory);
    }

    // Get all categories with pagination
    @Override
    public ResultPagingDTO getAllCategories(Specification<Category> spec, Pageable pageable) {
        // Fetch categories with specification and pagination
        Page<Category> categories = categoryRepository.findAll(spec, pageable);

        // Throw exception if no categories are found
        if (categories.isEmpty()) {
            throw new NotFoundException("No categories found");
        }

        // Map entities to DTOs
        List<CategoryDTO> categoryDTOs = categories.getContent().stream()
                .map(CategoryMapper.INSTANCE::convertToCategoryDTO)
                .collect(Collectors.toList());

        // Set pagination metadata
        ResultPagingDTO.Meta meta = new ResultPagingDTO.Meta();
        meta.setPage(pageable.getPageNumber());
        meta.setSize(pageable.getPageSize());
        meta.setTotal(categories.getTotalElements());
        meta.setTotalPages(categories.getTotalPages());

        ResultPagingDTO resultPagingDTO = new ResultPagingDTO();
        resultPagingDTO.setMeta(meta);
        resultPagingDTO.setData(categoryDTOs);

        // Return results with metadata
        return resultPagingDTO;
    }
}
