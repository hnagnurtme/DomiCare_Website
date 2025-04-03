package com.backend.domicare.dto.request;

import java.util.Set;

import com.backend.domicare.dto.RoleDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    private String id ;
    private String email;
    private String name;
    private String password;
    private String address;
    private String phone;

    private Set<RoleDTO> roles;
}
