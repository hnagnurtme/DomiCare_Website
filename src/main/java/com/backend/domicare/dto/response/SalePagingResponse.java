package com.backend.domicare.dto.response;

import java.time.Instant;

import com.backend.domicare.model.Gender;

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
public class SalePagingResponse {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String avatar;
    private String googleId;
    private Gender gender;
    private Boolean isActive;
    private Instant dateOfBirth;
    private String createBy;
    private String updateBy;
    private Instant createAt;
    private Instant updateAt;
    private Long sale_totalBookings;
    private Double sale_successPercent;
    
}
