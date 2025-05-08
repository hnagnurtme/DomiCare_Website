package com.backend.domicare.dto.request;

import java.time.Instant;

import com.backend.domicare.model.Gender;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    private String oldPassword;
    
    @Size(min = 6, message = "New password must be at least 6 characters")
    private String newPassword;
    
    private String address;
    
    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be between 10 and 15 digits")
    private String phone;
    
    private Gender gender;
    
    private Instant dateOfBirth;
    
    private Long imageId;
}
