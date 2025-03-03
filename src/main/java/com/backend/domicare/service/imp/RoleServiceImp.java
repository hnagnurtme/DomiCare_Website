package com.backend.domicare.service.imp;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.backend.domicare.model.Role;
import com.backend.domicare.repository.RolesRepository;
import com.backend.domicare.service.RoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImp  implements RoleService {
    private final RolesRepository roleRepository;
    @Override
    public Role getRoleById(Long id) {
       Optional<Role> role = roleRepository.findById(id);
       if (!role.isPresent()) {
           throw new IllegalArgumentException("Role not found");
       }
        return role.get();
    }
    @Override
    public Role getRoleByName(String name) {
        Role role = roleRepository.findByName(name);
        if(role == null) {
            throw new IllegalArgumentException("Role not found");
        }
        return role;
    }
    
}
