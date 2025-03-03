package com.backend.domicare.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter 
public class UpdateUserResponse {
    private String name;
    private String password;
    private String address;
    private String phone;
    private String email;
}
