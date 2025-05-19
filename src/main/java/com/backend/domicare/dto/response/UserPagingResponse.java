package com.backend.domicare.dto.response;

import java.time.Instant;
import java.util.Set;

import com.backend.domicare.dto.RoleDTO;
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
public class UserPagingResponse {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String avatar;
    private String googleId;
    private Gender gender;
    private Boolean isActive;
    private Instant dateOfBirth;
    private boolean isEmailConfirmed;
    private String emailConfirmationToken;
    private String createBy;
    private String updateBy;
    private Instant createAt;
    private Instant updateAt;
    private Set< RoleDTO >roles;
    private Long user_totalSuccessBookings;
    private Long user_totalFailedBookings;

    private Long sale_totalBookings;
    private Double sale_successPercent;
}
