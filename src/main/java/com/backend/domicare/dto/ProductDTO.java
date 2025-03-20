package com.backend.domicare.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String image;
    private CategoryDTO category;
}
