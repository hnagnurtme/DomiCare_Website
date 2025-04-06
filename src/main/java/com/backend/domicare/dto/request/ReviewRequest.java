package com.backend.domicare.dto.request;

import jakarta.validation.constraints.NotNull;
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
public class ReviewRequest {
    @NotNull(message = "productId không được để trống")
    private Long productId;
    @NotNull(message = "rating không được để trống")
    private Integer rating;
    private String comment;
}
