package com.backend.domicare.service.imp;

import java.util.List;
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

    // Fetch category by ID
    @Override
    public CategoryDTO fetchCategoryById(Long categoryId) {
        logger.info("Fetching category with ID: {}", categoryId);
        // Retrieve category, throw exception if not found
        Category category = categoryRepository.findById(categoryId)
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
        Long imageId = request.getImageId();
        Category categoryEntity = CategoryMapper.INSTANCE.convertToCategory(request);
        // Check if category already exists
        if (categoryRepository.existsByName(categoryEntity.getName())) {
            logger.error("Category already exists with name: {}", categoryEntity.getName());
            throw new CategoryAlreadyExists("Category already exists with name: " + categoryEntity.getName());
        }
        // If image ID is provided, set it in the category entity
        if (imageId != null) {
            File image = this.fileRepository.findById(imageId)
                .orElseThrow(() -> {
                    logger.error("Image not found with ID: {}", imageId);
                    return new NotFoundFileException("Image not found with ID: " + imageId);
                });
            categoryEntity.setImage(image.getUrl());
        }
        categoryEntity.setDeleted(false);
        categoryEntity.setNameUnsigned(FormatStringAccents.removeTones(request.getName()));
        categoryRepository.save(categoryEntity);
        logger.info("Category added successfully with name: {}", categoryEntity.getName());
        return CategoryMapper.INSTANCE.convertToCategoryDTO(categoryEntity);
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

        // If category has products, delete them in batch
        if (!category.getProducts().isEmpty()) {
            logger.info("Deleting products associated with category ID: {}", id);
            List<Long> productIds = category.getProducts().stream()
                .map(product -> product.getId())
                .collect(Collectors.toList());
            productRepository.softDeleteByCategoryIds(productIds);
        }

        // Delete the category
        categoryRepository.softDeleteById(id);
        logger.info("Category deleted successfully with ID: {}", id);
    }

    // Update category information
    @Override
    public CategoryDTO updateCategory(UpdateCategoryRequest request) {
        Long id = request.getCategoryId();
        logger.info("Updating category with ID: {}", id);
        Long imageId = request.getImageId();

        // Find existing category
        Category existingCategory = categoryRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Category not found with ID: {}", id);
                return new CategoryNotFoundException("Category not found with ID: " + id);
            });
        if (imageId != null) {
            File image = this.fileRepository.findById(imageId)
                .orElseThrow(() -> {
                    logger.error("Image not found with ID: {}", imageId);
                    return new NotFoundFileException("Image not found with ID: " + imageId);
                });
            existingCategory.setImage(image.getUrl());
        }
        // Check if category name already exists
        if (categoryRepository.existsByNameAndIdNot(request.getName(), id)) {
            logger.error("Category already exists with name: {}", request.getName());
            throw new CategoryAlreadyExists("Category already exists with name: " + request.getName());
        }
        // Update fields with values from DTO
        if (request.getName() != null) {
            existingCategory.setName(request.getName());
        }
        if (request.getDescription() != null) {
            existingCategory.setDescription(request.getDescription());
        }

        // Save updated category
        categoryRepository.save(existingCategory);
        logger.info("Category updated successfully with ID: {}", id);
        return CategoryMapper.INSTANCE.convertToCategoryDTO(existingCategory);
    }

    // Get all categories with pagination
    @Override
    public ResultPagingDTO getAllCategories(Specification<Category> spec, Pageable pageable) {
        logger.info("Fetching all categories with pagination");
        // Fetch categories with specification and pagination
        Page<Category> categories = categoryRepository.findAll(spec, pageable);

        // Throw exception if no categories are found
        if (categories.isEmpty()) {
            logger.error("No categories found");
            throw new CategoryNotFoundException("No categories found");
        }

        // Map entities to DTOs
        List<CategoryDTO> categoryDTOs = categories.getContent().stream()
                .map(CategoryMapper.INSTANCE::convertToCategoryDTO)
                .collect(Collectors.toList());

        // Set pagination metadata
        ResultPagingDTO.Meta meta = new ResultPagingDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setSize(pageable.getPageSize());
        meta.setTotal(categories.getTotalElements());
        meta.setTotalPages(categories.getTotalPages());

        ResultPagingDTO resultPagingDTO = new ResultPagingDTO();
        resultPagingDTO.setMeta(meta);
        resultPagingDTO.setData(categoryDTOs);

        logger.info("Categories fetched successfully with total: {}", categories.getTotalElements());
        // Return results with metadata
        return resultPagingDTO;
    }
}
