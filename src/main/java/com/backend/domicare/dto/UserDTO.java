package com.backend.domicare.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String name;
    private String password;
    private String address;
    private String phone;
    private String email;

    private String createBy;
    private String updateBy;
    private Instant createAt;
    private Instant updateAt;
}
