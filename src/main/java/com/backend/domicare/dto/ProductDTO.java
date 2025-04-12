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

public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String image;
    private Double ratingStar;
    private Double discount;
    private Double priceAfterDiscount;
    private List<String> landingImages;
    private Long categoryID;

    private List<ReviewDTO> reviewDTOs;
    private String createBy;
    private String updateBy;
    private Instant createAt;
    private Instant updateAt;
}
