package com.backend.domicare.service.imp;

import java.util.Optional;

import com.backend.domicare.dto.PermissionDTO;
import com.backend.domicare.mapper.PermissionMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.backend.domicare.dto.paging.ResultPagingDTO;
import com.backend.domicare.exception.NotFoundException;
import com.backend.domicare.model.Permission;
import com.backend.domicare.repository.PermissionsRepository;
import com.backend.domicare.service.PermissionService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class PermissionServiceImp implements PermissionService {
    private final PermissionsRepository permissionsRepository;
    @Override
    public boolean isPermissionExists(PermissionDTO permission) {
        return permissionsRepository.existsByModuleAndApiPathAndMethod(permission.getModule(), permission.getApiPath(), permission.getMethod()) 
            && !permissionsRepository.findById(permission.getId()).isPresent();
    }
    

    @Override
    public PermissionDTO createPermission(PermissionDTO permission) {
        Permission entity = PermissionMapper.INSTANCE.convertToPermission(permission);

        return PermissionMapper.INSTANCE.convertToPermissionDTO(this.permissionsRepository.save(entity));
    }

    @Override
    public PermissionDTO getPermissionById(Long id) {
        Optional<Permission> permission = permissionsRepository.findById(id);
        if( permission.isPresent() ) {
            return PermissionMapper.INSTANCE.convertToPermissionDTO(permission.get());
        }
        throw new NotFoundException("Permission not found");
    }

    @Override
    public ResultPagingDTO getPermissions(Specification<Permission> spec,Pageable pageable){
        Page<Permission> permissions = permissionsRepository.findAll(spec, pageable);
        ResultPagingDTO resultPagingDTO = new ResultPagingDTO();
        ResultPagingDTO.Meta meta = new ResultPagingDTO.Meta();

        meta.setPage(permissions.getNumber());
        meta.setSize(permissions.getSize());
        meta.setTotal(permissions.getTotalElements());
        meta.setTotalPages(permissions.getTotalPages());

        resultPagingDTO.setMeta(meta);
        resultPagingDTO.setData(permissions.getContent());
        return resultPagingDTO;
    }

    @Override
    public PermissionDTO updatePermission(PermissionDTO permission) {
        Permission oldPermission = this.permissionsRepository.findById(permission.getId()).orElseThrow(() -> new NotFoundException("Permission not found"));

        oldPermission.setName(permission.getName());
        oldPermission.setApiPath(permission.getApiPath());
        oldPermission.setMethod(permission.getMethod());
        oldPermission.setModule(permission.getModule());
        oldPermission.setUpdateBy(permission.getUpdateBy());
        oldPermission.setUpdateAt(permission.getUpdateAt());

        Permission updatedPermission = this.permissionsRepository.save(oldPermission);
        return PermissionMapper.INSTANCE.convertToPermissionDTO(updatedPermission);
    }
    
    @Override
    public void deletePermission(Long id) {
        Optional<Permission> permissionOptional = this.permissionsRepository.findById(id);
        if (permissionOptional.isEmpty()) {
            throw new NotFoundException("Permission not found");
        }
        Permission currentPermission = permissionOptional.get();
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));

        this.permissionsRepository.delete(currentPermission);
    }
}
