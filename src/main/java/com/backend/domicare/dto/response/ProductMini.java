package com.backend.domicare.dto.response;

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
public class ProductMini {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String image;
    private Double ratingStar;
    private Double discount;
    private Double priceAfterDiscount;
    private CategoryMini categoryMini;
}
