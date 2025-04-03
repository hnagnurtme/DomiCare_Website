package com.backend.domicare.dto;

import java.time.Instant;

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
public class RoleDTO {
    private Long id;
    private String name;
    private String description;
    private boolean active;
    private String createBy;
    private String updateBy;
    private Instant createAt;
    private Instant updateAt;

}
