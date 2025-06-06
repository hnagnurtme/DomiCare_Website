package com.backend.domicare.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.Instant;

import com.backend.domicare.model.Gender;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class AddUserByAdminRequest {
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email format is invalid")
    @Schema(example = "user@example.com", description = "User's email address")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$", message = "Password must be at least 6 characters and include lowercase, uppercase, and number")
    @Schema(example = "Password123", description = "Strong password with minimum requirements")
    private String password;

    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Schema(example = "John Doe", description = "User's full name")
    private String name;

    @Pattern(regexp = "^[0-9]{10,11}$", message = "Phone number must be 10 or 11 digits")
    @Schema(example = "0912345678", description = "User's phone number")
    private String phone;

    @Schema(example = "123 Street, City", description = "User's address")
    private String address;

    @Schema(example = "https://example.com/avatar.jpg", description = "URL of the user's avatar image")
    private String avatar;

    @Schema(example = "MALE", description = "Gender")
    private Gender gender;

    @NotNull(message = "Date of birth cannot be empty")
    @Schema(example = "2000-01-01T00:00:00Z", description = "User's date of birth in ISO 8601 format")
    private Instant dateOfBirth;

    @NotNull(message = "Role ID cannot be empty")
    @Positive(message = "Role ID must be positive")
    @Schema(example = "1", description = "ID of the role to assign to the user")
    private Long roleId;
}
