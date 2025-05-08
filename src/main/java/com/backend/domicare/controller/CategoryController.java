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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Category management APIs")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "Create a new category", description = "Creates a new category with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@Valid @RequestBody AddCategoryRequest categoryDTO) {
        CategoryDTO category = categoryService.addCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @Operation(summary = "Update an existing category", description = "Updates a category with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = Message.class))),
        @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @PutMapping("/categories")
    public ResponseEntity<?> updateCategory(@Valid @RequestBody UpdateCategoryRequest categoryDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.updateCategory(categoryDTO));
    }

    @Operation(summary = "Delete a category", description = "Deletes a category by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(
            @Parameter(description = "ID of the category to be deleted") @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new Message("Category deleted successfully"));
    }

    @Operation(summary = "Get a category by ID", description = "Returns a category based on its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category found",
                    content = @Content(schema = @Schema(implementation = CategoryDTO.class))),
        @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(schema = @Schema(implementation = Message.class)))
    })
    @GetMapping("/public/categories/{id}")
    public ResponseEntity<?> getCategoryById(
            @Parameter(description = "ID of the category to be retrieved") @PathVariable Long id) {
        CategoryDTO category = categoryService.fetchCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    @Operation(summary = "Get all categories", description = "Returns a paginated list of categories with filtering and sorting options")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of categories",
                    content = @Content(schema = @Schema(implementation = ResultPagingDTO.class)))
    })
    @GetMapping("/public/categories")
    public ResponseEntity<ResultPagingDTO> getCategories(
            @Parameter(description = "Page number (1-based)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Number of items per page") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Search by category name") @RequestParam(required = false) String searchName,
            @Parameter(description = "Field to sort by") @RequestParam(required = false, defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)") @RequestParam(required = false, defaultValue = "asc") String sortDirection,
            @Parameter(description = "Additional filter specification") @Filter Specification<Category> spec, 
            Pageable pageable) {

        // Validate pagination parameters
        if (page < 1) page = 1;
        if (size < 1 || size > 100) size = 20; // Set reasonable limits for page size
        
        // Create sort object
        Sort sort = Sort.by(sortBy);
        if (sortDirection.equalsIgnoreCase("desc")) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        pageable = PageRequest.of(page - 1, size, sort);

        // Apply search filter
        if (searchName != null && !searchName.trim().isEmpty()) {
            String cleanSearchName = FormatStringAccents.removeTones(searchName.toLowerCase().trim());
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder
                    .like(criteriaBuilder.lower(root.get("nameUnsigned")), "%" + cleanSearchName + "%"));
        }
        
        // Only show non-deleted categories
        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("isDeleted"), false));

        return ResponseEntity.status(HttpStatus.OK).body(this.categoryService.getAllCategories(spec, pageable));
    }
}
