package com.backend.domicare.service;



import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.backend.domicare.dto.paging.ResultPagingDTO;
import com.backend.domicare.model.Permission;

public interface PermissionService {
    public boolean isPermissionExists(Permission permission);
    public Permission createPermission(Permission permission);
    public Permission getPermissionById(Long id);

    public Permission updatePermission(Permission permission);
    public void deletePermission(Permission permission);

    public ResultPagingDTO getPermissions(Specification<Permission> spec,Pageable pageable);
}
