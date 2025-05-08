package com.backend.domicare.dto;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Data transfer object for category information")
public class CategoryDTO {
    @Schema(description = "Category ID", example = "1")
    private Long id;
    
    @Schema(description = "Category name", example = "Sản phẩm khô")
    private String name;
    
    @Schema(description = "Category description", example = "Các sản phẩm khô và đóng gói")
    private String description;
    
    @Schema(description = "List of products in this category")
    private List<ProductDTO> products;
    
    @Schema(description = "URL of the category image", example = "https://example.com/images/category.jpg")
    private String image;

    @Schema(description = "Username of who created the category", example = "admin")
    private String createBy;
    
    @Schema(description = "Username of who last updated the category", example = "admin")
    private String updateBy;
    
    @Schema(description = "Timestamp when category was created", example = "2023-01-01T10:00:00Z")
    private Instant createAt;
    
    @Schema(description = "Timestamp when category was last updated", example = "2023-01-10T15:30:00Z")
    private Instant updateAt;
}
