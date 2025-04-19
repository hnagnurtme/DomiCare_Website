package com.backend.domicare.dto.request;

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
public class UpdateUserRequest {
    private String name;
    private String oldPassword;
    private String newPassword;
    private String address;
    private String phone;
    private Gender gender;
    private Instant dateOfBirth;
    private Long imageId;
}
