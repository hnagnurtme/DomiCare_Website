package com.backend.domicare.mapper;

import com.backend.domicare.dto.PermissionDTO;
import com.backend.domicare.model.Permission;
import com.backend.domicare.model.Role;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;

public interface PermissionMapper {
    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);
    default PermissionDTO convertToPermissionDTO(Permission permission){
        if (permission == null) return null;
        return PermissionDTO.builder()
                .id(permission.getId())
                .name(permission.getName())
                .apiPath(permission.getApiPath())
                .updateAt(permission.getUpdateAt())
                .createAt(permission.getCreateAt())
                .createBy(permission.getCreateBy())
                .updateBy(permission.getUpdateBy())
                .method(permission.getMethod())
                .module(permission.getModule())
                .roleDTOList(RoleMapper.INSTANCE.convertToRoleDTOList(permission.getRoles()))
                .build();
    }

    Permission convertToPermission(PermissionDTO permissionDTO);
    List<PermissionDTO> convertToPermissionDTOList(List<Permission> permissions);
    List<Permission> convertToPermissionList(List<PermissionDTO> permissionDTOs);
}
