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
import com.backend.domicare.dto.request.AddCategoryRequest;
import com.backend.domicare.exception.CategoryAlreadyExists;
import com.backend.domicare.exception.CategoryNotFoundException;
import com.backend.domicare.exception.NotFoundFileException;
import com.backend.domicare.mapper.CategoryMapper;
import com.backend.domicare.model.Category;
import com.backend.domicare.model.File;
import com.backend.domicare.repository.CategoriesRepository;
import com.backend.domicare.repository.FilesRepository;
import com.backend.domicare.repository.ProductsRepository;
import com.backend.domicare.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImp implements CategoryService {

    private final CategoriesRepository categoryRepository;
    private final ProductsRepository productRepository;
    private final FilesRepository fileRepository;

    // Fetch category by ID
    @Override
    public CategoryDTO fetchCategoryById(Long categoryId) {
        // Retrieve category, throw exception if not found
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + categoryId));
        return CategoryMapper.INSTANCE.convertToCategoryDTO(category);
    }

    // Add a new category
    @Override
    public CategoryDTO addCategory(AddCategoryRequest request){
        // Convert request to DTO
        CategoryDTO categoryDTO =  request.getCategory();
        Long imageId = request.getImageId();
        // Convert DTO to entity and save
        Category categoryEntity = CategoryMapper.INSTANCE.convertToCategory(categoryDTO);
        // Check if category already exists
        if (categoryRepository.existsByName(categoryEntity.getName())) {
            throw new CategoryAlreadyExists("Category already exists with name: " + categoryEntity.getName());
        }
        // If image ID is provided, set it in the category entity
        if (imageId != null) {
            File image = this.fileRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundFileException("Image not found with ID: " + imageId));
            categoryEntity.setImage(image.getUrl());
        }
        categoryRepository.save(categoryEntity);
        return CategoryMapper.INSTANCE.convertToCategoryDTO(categoryEntity);
    }

    // Delete category and its associated products
    @Transactional
    @Override
    public void deleteCategory(Long id) {
        // Check if category exists
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));

        // If category has products, delete them in batch
        if (!category.getProducts().isEmpty()) {
            // Deletes all products associated with the category
            List<Long> productIds = category.getProducts().stream()
                .map(product -> product.getId())
                .collect(Collectors.toList());
            productRepository.deleteAllById(productIds);
            // Alternatively, if you want to delete products by category ID:
            // Deletes all products by category ID
        }

        // Delete the category
        categoryRepository.deleteById(id);
    }

    // Update category information
    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        // Find existing category
        Category existingCategory = categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException("Category not found with ID: " + id));

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
            throw new CategoryNotFoundException("No categories found");
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
