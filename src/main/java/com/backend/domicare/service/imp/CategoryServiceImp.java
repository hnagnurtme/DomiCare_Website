package com.backend.domicare.service.imp;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.domicare.dto.CategoryDTO;
import com.backend.domicare.dto.paging.ResultPagingDTO;
import com.backend.domicare.dto.request.AddCategoryRequest;
import com.backend.domicare.dto.request.UpdateCategoryRequest;
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
import com.backend.domicare.utils.FormatStringAccents;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImp implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImp.class);

    private final CategoriesRepository categoryRepository;
    private final ProductsRepository productRepository;
    private final FilesRepository fileRepository;

    @Override
    public CategoryDTO fetchCategoryById(Long categoryId) {
        logger.info("Fetching category with ID: {}", categoryId);
        
        // Sử dụng Optional API để xử lý trường hợp không tìm thấy
        Category category = Optional.ofNullable(categoryRepository.findByIdAndNotDeleted(categoryId))
            .orElseThrow(() -> {
                logger.error("Category not found with ID: {}", categoryId);
                return new CategoryNotFoundException("Category not found with ID: " + categoryId);
            });
            
        logger.info("Category fetched successfully with ID: {}", categoryId);
        return CategoryMapper.INSTANCE.convertToCategoryDTO(category);
    }

    // Add a new category
    @Override
    public CategoryDTO addCategory(AddCategoryRequest request) {
        logger.info("Adding new category with name: {}", request.getName());
        
        // Validate category name doesn't already exist (case insensitive)
        String categoryName = request.getName().trim();
        validateCategoryNameIsUnique(categoryName);
        
        // Create category entity and set base properties
        Category categoryEntity = CategoryMapper.INSTANCE.convertToCategory(request);
        categoryEntity.setDeleted(false);
        categoryEntity.setNameUnsigned(FormatStringAccents.removeTones(categoryName));
        
        // Set image if provided
        if (request.getImageId() != null) {
            setImageForCategory(categoryEntity, request.getImageId());
        }
        
        // Save and return result
        Category savedCategory = categoryRepository.save(categoryEntity);
        logger.info("Category added successfully with ID: {}", savedCategory.getId());
        return CategoryMapper.INSTANCE.convertToCategoryDTO(savedCategory);
    }
    
    /**
     * Helper method to validate that a category name is unique
     * 
     * @param categoryName the name to validate
     * @throws CategoryAlreadyExists if a category with the same name already exists
     */
    private void validateCategoryNameIsUnique(String categoryName) {
        if (categoryRepository.existsByName(categoryName)) {
            logger.error("Category already exists with name: {}", categoryName);
            throw new CategoryAlreadyExists("Category already exists with name: " + categoryName);
        }
    }
    
    /**
     * Sets an image for a category entity based on the image ID
     * 
     * @param category the category entity to update
     * @param imageId the ID of the image to set
     * @throws NotFoundFileException if the image is not found
     */
    private void setImageForCategory(Category category, Long imageId) {
        File image = fileRepository.findById(imageId)
            .orElseThrow(() -> {
                logger.error("Image not found with ID: {}", imageId);
                return new NotFoundFileException("Image not found with ID: " + imageId);
            });
        category.setImage(image.getUrl());
    }

    // Delete category and its associated products
    @Transactional
    @Override
    public void deleteCategory(Long id) {
        logger.info("Deleting category with ID: {}", id);
        
        // Check if category exists
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Category not found with ID: {}", id);
                return new CategoryNotFoundException("Category not found with ID: " + id);
            });

        // Delete associated products first
        deleteAssociatedProducts(category);

        // Delete the category
        categoryRepository.softDeleteById(id);
        logger.info("Category deleted successfully with ID: {}", id);
    }
    
    /**
     * Delete all products associated with a category
     * 
     * @param category the category containing products to delete
     */
    private void deleteAssociatedProducts(Category category) {
        if (category.getProducts() != null && !category.getProducts().isEmpty()) {
            logger.info("Deleting {} products associated with category ID: {}", 
                        category.getProducts().size(), category.getId());
            
            List<Long> productIds = category.getProducts().stream()
                .map(product -> product.getId())
                .collect(Collectors.toList());
                
            productRepository.softDeleteByCategoryIds(productIds);
        }
    }

    // Update category information
    @Override
    public CategoryDTO updateCategory(UpdateCategoryRequest request) {
        Long id = request.getCategoryId();
        logger.info("Updating category with ID: {}", id);

        // Find existing category
        Category existingCategory = categoryRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Category not found with ID: {}", id);
                return new CategoryNotFoundException("Category not found with ID: " + id);
            });
        
        // Handle image update if provided
        if (request.getImageId() != null) {
            setImageForCategory(existingCategory, request.getImageId());
        }
        
        // Update name if provided, checking uniqueness
        String newName = request.getName();
        if (newName != null && !newName.equals(existingCategory.getName())) {
            updateCategoryName(existingCategory, newName.trim(), id);
        }
        
        // Update description if provided
        if (request.getDescription() != null) {
            existingCategory.setDescription(request.getDescription().trim());
        }

        // Save updated category
        Category updatedCategory = categoryRepository.save(existingCategory);
        logger.info("Category updated successfully with ID: {}", id);
        return CategoryMapper.INSTANCE.convertToCategoryDTO(updatedCategory);
    }
    
    /**
     * Update a category's name and nameUnsigned while checking for uniqueness
     * 
     * @param category the category to update
     * @param newName the new name value
     * @param categoryId the ID of the category being updated
     * @throws CategoryAlreadyExists if another category has the same name
     */
    private void updateCategoryName(Category category, String newName, Long categoryId) {
        // Validate the new name is unique
        if (categoryRepository.existsByNameAndIdNot(newName, categoryId)) {
            logger.error("Category already exists with name: {}", newName);
            throw new CategoryAlreadyExists("Category already exists with name: " + newName);
        }
        
        // Update name and its unsigned version
        category.setName(newName);
        category.setNameUnsigned(FormatStringAccents.removeTones(newName));
    }

    // Get all categories with pagination
    @Override
    public ResultPagingDTO getAllCategories(Specification<Category> spec, Pageable pageable) {
        logger.info("Fetching all categories with pagination");
        
        // Fetch categories with specification and pagination
        Page<Category> categoriesPage = categoryRepository.findAll(spec, pageable);

        // Log empty results but don't throw exception - empty result is valid
        if (categoriesPage.isEmpty()) {
            logger.info("No categories found for the given criteria");
            return createEmptyPagingResult(pageable);
        }

        // Map entities to DTOs
        List<CategoryDTO> categoryDTOs = categoriesPage.getContent().stream()
                .map(CategoryMapper.INSTANCE::convertToCategoryDTO)
                .collect(Collectors.toList());

        logger.info("Categories fetched successfully with total: {}", categoriesPage.getTotalElements());
        // Return results with metadata
        return createPagingResult(categoryDTOs, categoriesPage, pageable);
    }
    
    /**
     * Create a paging result with the given data and metadata
     * 
     * @param data the list of DTOs to include in the result
     * @param page the Page object containing metadata
     * @param pageable the Pageable object used in the query
     * @return a ResultPagingDTO with data and metadata
     */
    private ResultPagingDTO createPagingResult(List<?> data, Page<?> page, Pageable pageable) {
        ResultPagingDTO.Meta meta = new ResultPagingDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1); // Convert 0-based to 1-based for client
        meta.setSize(pageable.getPageSize());
        meta.setTotal(page.getTotalElements());
        meta.setTotalPages(page.getTotalPages());

        ResultPagingDTO resultPagingDTO = new ResultPagingDTO();
        resultPagingDTO.setMeta(meta);
        resultPagingDTO.setData(data);
        
        return resultPagingDTO;
    }
    
    /**
     * Create an empty paging result with just metadata
     * 
     * @param pageable the Pageable object used in the query
     * @return a ResultPagingDTO with empty data and metadata
     */
    private ResultPagingDTO createEmptyPagingResult(Pageable pageable) {
        ResultPagingDTO.Meta meta = new ResultPagingDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setSize(pageable.getPageSize());
        meta.setTotal(0);
        meta.setTotalPages(0);

        ResultPagingDTO resultPagingDTO = new ResultPagingDTO();
        resultPagingDTO.setMeta(meta);
        resultPagingDTO.setData(List.of());
        
        return resultPagingDTO;
    }
}
