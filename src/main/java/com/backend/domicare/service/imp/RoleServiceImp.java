package com.backend.domicare.service.imp;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.backend.domicare.dto.RoleDTO;
import com.backend.domicare.exception.NotFoundException;
import com.backend.domicare.exception.NotFoundRoleException;
import com.backend.domicare.exception.RoleAlreadyExists;
import com.backend.domicare.mapper.RoleMapper;
import com.backend.domicare.model.Permission;
import com.backend.domicare.model.Role;
import com.backend.domicare.repository.PermissionsRepository;
import com.backend.domicare.repository.RolesRepository;
import com.backend.domicare.service.RoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImp  implements RoleService {
    private final RolesRepository roleRepository;
    private final PermissionsRepository permissionsRepository;
    @Override
    public RoleDTO getRoleById(Long id) {
       Optional<Role> role = roleRepository.findById(id);
       if (!role.isPresent()) {
           throw new NotFoundRoleException( "Role not found for id: " + id);
       }
        return RoleMapper.INSTANCE.convertToRoleDTO(role.get());
    }
    @Override
    public Role getRoleByName(String name) {
        Role role = roleRepository.findByName(name);
        if(role == null) {
            throw new NotFoundRoleException("Role not found for name: " + name);
        }
        return role;
    }

    @Override
    public boolean isRoleExistsByName(String name) {
        return roleRepository.existsByName(name);
    }

    @Override
    public RoleDTO createRole(RoleDTO role) {
        Role roleEntity = RoleMapper.INSTANCE.convertToRole(role);
        if( roleEntity.getPermissions() != null){
            List<Long> reqPermissions = roleEntity.getPermissions().stream()
            .map(permission -> permission.getId())
            .collect(Collectors.toList());

            List<Permission> permissions = this.permissionsRepository.findByIdIn(reqPermissions);
            if( permissions.size() != reqPermissions.size() ) {
                throw new NotFoundException("Some permissions not found");
            }
            roleEntity.setPermissions(permissions);
        }
        if( this.isRoleExistsByName(roleEntity.getName()) ) {
            throw new RoleAlreadyExists("Role already exists");
        }

        Role newRole = roleRepository.save(roleEntity);
        return RoleMapper.INSTANCE.convertToRoleDTO(newRole);
    }
    
    @Override
    public RoleDTO updateRole(RoleDTO role) {
        Role oldRole = roleRepository.findById(role.getId())
                .orElseThrow(() -> new NotFoundRoleException("Role not found for id: " + role.getId()));
        if ( role.getName()!= null) {
            oldRole.setName(role.getName());
        }
        
        if ( role.getDescription() != null) {
            oldRole.setDescription(role.getDescription());
        }

        oldRole.setActive(role.isActive());
        
        if ( role.getCreateBy() != null) {
            oldRole.setCreateBy(role.getCreateBy());
        }
        if ( role.getUpdateBy() != null) {
            oldRole.setUpdateBy(role.getUpdateBy());
        }
        if ( role.getCreateAt() != null) {
            oldRole.setCreateAt(role.getCreateAt());
        }
        if ( role.getUpdateAt() != null) {
            oldRole.setUpdateAt(role.getUpdateAt());
        }
        if ( role.getId() != null) {
            oldRole.setId(role.getId());
        }
        if ( role.getName() != null) {
            oldRole.setName(role.getName());
        }
        if ( role.getDescription() != null) {
            oldRole.setDescription(role.getDescription());
        }
        Role roleUpdate = RoleMapper.INSTANCE.convertToRole(role);
        if( roleUpdate.getPermissions() != null){
            List<Long> reqPermissions = roleUpdate.getPermissions().stream()
            .map(permission -> permission.getId())
            .collect(Collectors.toList());

            List<Permission> permissions = this.permissionsRepository.findByIdIn(reqPermissions);
            if( permissions.size() != reqPermissions.size() ) {
                throw new NotFoundException("Some permissions not found");
            }
            roleUpdate.setPermissions(permissions);
        }

        oldRole.setPermissions(roleUpdate.getPermissions());
        Role updatedRole = roleRepository.save(oldRole);
        return RoleMapper.INSTANCE.convertToRoleDTO(updatedRole);
    }

    @Override
    public void deleteRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (!role.isPresent()) {
            throw new NotFoundRoleException("Role not found for id: " + id);
        }
        roleRepository.deleteById(id);
    }

    @Override
    public Set<RoleDTO> getRoles() {
        return RoleMapper.INSTANCE.convertToRoleDTOSet(roleRepository.findAll().stream().collect(Collectors.toSet()));
    }

    @Override
    public Role getRoleEntityById(Long id){
        Optional<Role> role = roleRepository.findById(id);
        if (!role.isPresent()) {
            throw new NotFoundRoleException("Role not found for id: " + id);
        }
        return role.get();
    }

    
}
