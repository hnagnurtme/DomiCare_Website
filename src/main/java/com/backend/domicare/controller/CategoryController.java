package com.backend.domicare.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domicare.dto.CategoryDTO;
import com.backend.domicare.dto.paging.ResultPagingDTO;
import com.backend.domicare.dto.request.AddCategoryRequest;
import com.backend.domicare.dto.request.UpdateCategoryRequest;
import com.backend.domicare.dto.response.Message;
import com.backend.domicare.model.Category;
import com.backend.domicare.service.CategoryService;
import com.backend.domicare.utils.FormatStringAccents;
import com.turkraft.springfilter.boot.Filter;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@Valid @RequestBody AddCategoryRequest categoryDTO) {
        CategoryDTO category = categoryService.addCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PutMapping("/categories")
    public ResponseEntity<?> updateCategory(@Valid @RequestBody UpdateCategoryRequest categoryDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.updateCategory(categoryDTO));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Message("Category deleted successfully"));
    }

    @GetMapping("/public/categories/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = categoryService.fetchCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @GetMapping("/public/categories")
    public ResponseEntity<ResultPagingDTO> getCategories(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String searchName,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection,
            @Filter Specification<Category> spec, Pageable pageable) {

        Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        pageable = PageRequest.of(page - 1, size, sort);

        if (searchName != null && !searchName.isEmpty()) {

            String cleanSearchName = FormatStringAccents.removeTones(searchName.toLowerCase());

            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("nameUnsigned")), "%" + cleanSearchName + "%"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.getAllCategories(spec, pageable));
    }
}
