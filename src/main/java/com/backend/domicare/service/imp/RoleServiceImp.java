package com.backend.domicare.service.imp;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.backend.domicare.model.Role;
import com.backend.domicare.repository.PermissionsRepository;
import com.backend.domicare.repository.RolesRepository;
import com.backend.domicare.service.RoleService;
import com.backend.domicare.exception.NotFoundException;
import com.backend.domicare.model.Permission;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImp  implements RoleService {
    private final RolesRepository roleRepository;
    private final PermissionsRepository permissionsRepository;
    @Override
    public Role getRoleById(Long id) {
       Optional<Role> role = roleRepository.findById(id);
       if (!role.isPresent()) {
           throw new NotFoundException( "Role not found");
       }
        return role.get();
    }
    @Override
    public Role getRoleByName(String name) {
        Role role = roleRepository.findByName(name);
        if(role == null) {
            throw new NotFoundException("Role not found");
        }
        return role;
    }

    @Override
    public boolean isRoleExistsByName(String name) {
        return roleRepository.existsByName(name);
    }

    @Override
    public Role createRole(Role role) {
        if( role.getPermissions() != null){
            List<Long> reqPermissions = role.getPermissions().stream()
            .map(permission -> permission.getId())
            .collect(Collectors.toList());

            List<Permission> permissions = this.permissionsRepository.findByIdIn(reqPermissions);
            if( permissions.size() != reqPermissions.size() ) {
                throw new NotFoundException("Some permissions not found");
            }
            role.setPermissions(permissions);
        }

        return roleRepository.save(role);
    }
    
    @Override
    public Role updateRole(Role role) {
        Role oldRole = this.getRoleById(role.getId());
        if( oldRole == null ) {
            throw new NotFoundException("Role not found");
        }
        if( role.getPermissions() != null){
            List<Long> reqPermissions = role.getPermissions().stream()
            .map(permission -> permission.getId())
            .collect(Collectors.toList());

            List<Permission> permissions = this.permissionsRepository.findByIdIn(reqPermissions);
            if( permissions.size() != reqPermissions.size() ) {
                throw new NotFoundException("Some permissions not found");
            }
            oldRole.setPermissions(permissions);
        }
        oldRole.setName(role.getName());
        oldRole.setDescription(role.getDescription());
        return roleRepository.save(oldRole);
    }

    @Override
    public void deleteRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (!role.isPresent()) {
            throw new NotFoundException("Role not found");
        }
        roleRepository.delete(role.get());
    }

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}
