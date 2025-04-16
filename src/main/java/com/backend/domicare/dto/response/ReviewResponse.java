package com.backend.domicare.dto.response;

import java.time.Instant;

import com.backend.domicare.dto.UserDTO;

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
public class ReviewResponse {
    private Long id;
    private Integer rating;
    private String comment;
    private Long productId;
    private UserDTO userDTO;
    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;
    
}
