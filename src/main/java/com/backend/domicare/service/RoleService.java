package com.backend.domicare.service;

import com.backend.domicare.model.Role;

public interface RoleService {
    public Role getRoleById(Long id) ;
    public Role getRoleByName(String name);
}
