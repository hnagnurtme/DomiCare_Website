package com.backend.domicare.service;

import java.util.List;

import com.backend.domicare.model.Role;

public interface RoleService {
    public Role getRoleById(Long id) ;
    public Role getRoleByName(String name);
    public boolean isRoleExistsByName(String name);
    public Role createRole(Role role);
    public Role updateRole(Role role);
    public void deleteRoleById(Long id);
    public List<Role> getRoles();
}
