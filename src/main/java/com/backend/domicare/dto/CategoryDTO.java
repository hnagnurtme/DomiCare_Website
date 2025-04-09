package com.backend.domicare.dto;

import java.time.Instant;
import java.util.List;

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
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private List<ProductDTO> products;
    private String image;

    private String createBy;
    private String updateBy;
    private Instant createAt;
    private Instant updateAt;
}
