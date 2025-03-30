package com.backend.domicare.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
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
import com.backend.domicare.dto.response.Message;
import com.backend.domicare.model.Category;
import com.backend.domicare.service.CategoryService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@Transactional
@RequiredArgsConstructor
public class CategoryController {
    private  final CategoryService categoryService;

    //Add category
    @PostMapping("/categories")
    public ResponseEntity<?> createCategory (@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO category = categoryService.addCategory(categoryDTO);
        return  ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    //Update category
    @PutMapping("/categories")
    public ResponseEntity<?> updateCategory (@Valid @RequestBody CategoryDTO categoryDTO) {
        Long id = categoryDTO.getId();
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Category ID is required");
        }
        CategoryDTO category = categoryService.updateCategory(id,categoryDTO);
        return  ResponseEntity.status(HttpStatus.OK).body(category);
    }

    //Delete category
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory (@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return  ResponseEntity.status(HttpStatus.OK).body(new Message("Category deleted successfully"));
    }

    //Get category by id
    @GetMapping("/categories/{id}")
    public ResponseEntity<?> getCategoryById (@PathVariable Long id) {
        CategoryDTO category = categoryService.fetchCategoryById(id);
        return  ResponseEntity.status(HttpStatus.OK).body(category);
    }

    //
    @GetMapping("/categories")
    public ResponseEntity<ResultPagingDTO> getCategories(
        @RequestParam(required = false) String search,
        @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
    Specification<Category> spec = Specification.where(null);

    if (search != null && !search.isBlank()) {
        String[] criteria = search.split(",");

        for (String criterion : criteria) {
            String[] parts = criterion.split(":|<|>");

            if (parts.length == 2) {
                String key = parts[0];
                String value = parts[1];

                if (criterion.contains(":")) {
                    spec = spec.and((root, query, cb) -> cb.like(root.get(key), "%" + value + "%"));
                } else if (criterion.contains(">")) {
                    spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get(key), value));
                } else if (criterion.contains("<")) {
                    spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get(key), value));
                }
            }
        }
    }
        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.getAllCategories(spec, pageable));
    }
}
