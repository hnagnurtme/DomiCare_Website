package com.backend.domicare.dto.response;

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
public class UserMini {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String avatar;
    private Gender gender;
}
