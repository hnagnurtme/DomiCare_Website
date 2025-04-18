package com.backend.domicare.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.backend.domicare.dto.RoleDTO;
import com.backend.domicare.model.Role;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
    Role convertToRole(RoleDTO roleDTO);
    RoleDTO convertToRoleDTO(Role role);
    Set<RoleDTO> convertToRoleDTOSet(Set<Role> roles);
    Set<Role> convertToRoleSet(Set<RoleDTO> roleDTOs);
    List<RoleDTO> convertToRoleDTOList(List<Role> roles);
}
