package com.backend.domicare.dto;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDTO {
    private Long id;
    private String name;
    private String apiPath;
    private String method;
    private String module;

    private String createBy;
    private String updateBy;
    private Instant createAt;
    private Instant updateAt;
    private List<RoleDTO> roleDTOList;
}
