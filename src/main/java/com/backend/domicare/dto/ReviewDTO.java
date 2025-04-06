package com.backend.domicare.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class  ReviewDTO {
    private Long id;
    private Integer rating;
    private String comment;

    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;
    
    private Long userId;
    private Long productId;

}