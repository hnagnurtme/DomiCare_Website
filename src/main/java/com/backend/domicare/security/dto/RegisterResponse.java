package com.backend.domicare.security.dto;

import java.util.Set;

import com.backend.domicare.model.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private Long id;
    private String email;
    private String password;
    private String avatar;
    private String accessToken;
    private String refreshToken;
    private Set<Role> roles;
    private boolean isEmailConfirmed;

}
