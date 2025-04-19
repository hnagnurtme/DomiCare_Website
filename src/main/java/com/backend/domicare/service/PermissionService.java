package com.backend.domicare.service;



import com.backend.domicare.dto.PermissionDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.backend.domicare.dto.paging.ResultPagingDTO;
import com.backend.domicare.model.Permission;

public interface PermissionService {
    public boolean isPermissionExists(PermissionDTO permission);
    public PermissionDTO createPermission(PermissionDTO permission);
    public PermissionDTO getPermissionById(Long id);

    public PermissionDTO updatePermission(PermissionDTO permission);
    public void deletePermission(Long id);

    public ResultPagingDTO getPermissions(Specification<Permission> spec,Pageable pageable);
}
