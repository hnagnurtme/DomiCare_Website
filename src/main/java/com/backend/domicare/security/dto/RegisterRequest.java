package com.backend.domicare.security.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotEmpty(message = "{email_not_empty}")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{email_invalid}")
    @Schema( example = "string")
    private String email;

    @NotEmpty(message = "{password_not_empty}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$", message = "{password_invalid}")
    @Schema(example = "string")
    private String password;


    @NotEmpty(message = "{confirm_password_not_empty}")
    @Schema(example = "string")
    private String confirmPassword;
}
