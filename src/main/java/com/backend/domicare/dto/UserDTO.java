package com.backend.domicare.dto;

import com.backend.domicare.model.Role;

import java.time.Instant;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private boolean isEmailConfirmed;
    private String emailConfirmationToken;
    private String createBy;
    private String updateBy;
    private Instant createAt;
    private Instant updateAt;
    private Set< Role >roles;
}
