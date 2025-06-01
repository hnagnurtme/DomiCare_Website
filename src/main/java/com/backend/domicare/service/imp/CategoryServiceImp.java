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
import com.backend.domicare.model.Product;
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
        logger.info("[Category] Fetching category with ID: {}", categoryId);
    
        Category category = Optional.ofNullable(categoryRepository.findByIdAndNotDeleted(categoryId))
            .orElseThrow(() -> {
                logger.error("[Category] Category not found with ID: {}", categoryId);
                return new CategoryNotFoundException("Category not found with ID: " + categoryId);
            });
            
        logger.info("[Category] Category fetched successfully with ID: {}", categoryId);
        return CategoryMapper.INSTANCE.convertToCategoryDTO(category);
    }

    @Override
    public CategoryDTO addCategory(AddCategoryRequest request) {
        logger.info("[Category] Adding new category with name: {}", request.getName());

        String categoryName = request.getName().trim();
        validateCategoryNameIsUnique(categoryName);
        
        Category categoryEntity = CategoryMapper.INSTANCE.convertToCategory(request);
        categoryEntity.setDeleted(false);
        categoryEntity.setNameUnsigned(FormatStringAccents.removeTones(categoryName));
        
        if (request.getImageId() != null) {
            setImageForCategory(categoryEntity, request.getImageId());
        }
        
        Category savedCategory = categoryRepository.save(categoryEntity);
        logger.info("[Category] Category added successfully with ID: {}", savedCategory.getId());
        return CategoryMapper.INSTANCE.convertToCategoryDTO(savedCategory);
    }
    
    private void validateCategoryNameIsUnique(String categoryName) {
        if (categoryRepository.existsByName(categoryName)) {
            logger.error("[Category] Category already exists with name: {}", categoryName);
            throw new CategoryAlreadyExists("Category already exists with name: " + categoryName);
        }
    }
    
    private void setImageForCategory(Category category, String imageId) {
        File image = fileRepository.findByUrl(imageId);
        if( image == null) {
            logger.error("[Category] Image not found with ID: {}", imageId);
            throw new NotFoundFileException("Image not found with ID: " + imageId);
        }
        category.setImage(image.getUrl());
    }

    @Transactional
    @Override
    public void deleteCategory(Long id) {
        logger.info("[Category] Deleting category with ID: {}", id);
        
        Category category = categoryRepository.findByIdAndNotDeleted(id);
        if( category == null) {
            logger.error("[Category] Category not found with ID: {}", id);
            throw new CategoryNotFoundException("Category not found with ID: " + id);
        }
        logger.info("[Category] Found category to delete with ID: {}", id);

        deleteAssociatedProducts(category);

        categoryRepository.softDeleteById(id);
        logger.info("[Category] Category deleted successfully with ID: {}", id);
    }
    @Transactional
    private void deleteAssociatedProducts(Category category) {
        List<Product> products = productRepository.findByCategoryIdAndNotDeleted(category.getId());
        if (products == null || products.isEmpty()) {
            logger.info("[Category] No products found to delete for category ID: {}", category.getId());
            return;
        }
        List<Long> productIds = products.stream()
            .map(Product::getId)
            .collect(Collectors.toList());
        productRepository.softDeleteByIds(productIds);
        logger.info("[Category] Deleted {} products associated with category ID: {}", products.size(), category.getId());
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(UpdateCategoryRequest request) {
        Long id = request.getCategoryId();
        logger.info("[Category] Updating category with ID: {}", id);

        Category existingCategory = categoryRepository.findByIdAndNotDeleted(id);
        if (existingCategory == null) {
            logger.error("[Category] Category not found with ID: {}", id);
            throw new CategoryNotFoundException("Category not found with ID: " + id);
        }
        logger.info("[Category] Found category to update with ID: {}", id);
        
        if (request.getImageId() != null) {
            setImageForCategory(existingCategory, request.getImageId());
        }
        
        String newName = request.getName();
        if (newName != null && !newName.equals(existingCategory.getName())) {
            updateCategoryName(existingCategory, newName.trim(), id);
        }
        
        if (request.getDescription() != null) {
            existingCategory.setDescription(request.getDescription().trim());
        }

        Category updatedCategory = categoryRepository.save(existingCategory);
        logger.info("[Category] Category updated successfully with ID: {}", id);
        return CategoryMapper.INSTANCE.convertToCategoryDTO(updatedCategory);
    }
    
    private void updateCategoryName(Category category, String newName, Long categoryId) {
        if (categoryRepository.existsByNameAndIdNot(newName, categoryId)) {
            logger.error("[Category] Category already exists with name: {}", newName);
            throw new CategoryAlreadyExists("Category already exists with name: " + newName);
        }
        category.setName(newName);
        category.setNameUnsigned(FormatStringAccents.removeTones(newName));
    }

    @Override
    public ResultPagingDTO getAllCategories(Specification<Category> spec, Pageable pageable) {
        logger.info("[Category] Fetching all categories with pagination");
        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isDeleted"), false));
        Page<Category> categoriesPage = categoryRepository.findAll(spec, pageable);

        List<CategoryDTO> categories = CategoryMapper.INSTANCE.convertToCategoryDTOs(categoriesPage.getContent());

        ResultPagingDTO result = new ResultPagingDTO();
        ResultPagingDTO.Meta meta = new ResultPagingDTO.Meta();
        meta.setPage(categoriesPage.getNumber() + 1);
        meta.setSize(categoriesPage.getSize());
        meta.setTotal(categoriesPage.getTotalElements());
        meta.setTotalPages(categoriesPage.getTotalPages());
        result.setMeta(meta);
        result.setData(categories);

        logger.info("[Category] Categories fetched successfully with total: {}", categoriesPage.getTotalElements());
        return result;
    }
}
