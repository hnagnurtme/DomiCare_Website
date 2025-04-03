package com.backend.domicare.service;

import java.util.Set;

import com.backend.domicare.dto.RoleDTO;
import com.backend.domicare.model.Role;

public interface RoleService {
    public RoleDTO getRoleById(Long id) ;
    public Role getRoleByName(String name);
    public boolean isRoleExistsByName(String name);
    public RoleDTO createRole(RoleDTO role);
    public RoleDTO updateRole(RoleDTO role);
    public void deleteRoleById(Long id);
    public Set<RoleDTO> getRoles();
}
