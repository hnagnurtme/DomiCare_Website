package com.backend.domicare.controller;


import org.hibernate.annotations.Filter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;
import com.backend.domicare.model.Permission;
import com.backend.domicare.service.PermissionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping("/permissions")
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission permission) {

        if( this.permissionService.isPermissionExists(permission) ) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.createPermission(permission));
    }

    @PutMapping("/permissions")
    public ResponseEntity<Permission> updatePermission(@Valid @RequestBody Permission permission) {

        if( this.permissionService.getPermissionById(permission.getId()) == null) {
            return ResponseEntity.badRequest().build();
        }

        if( this.permissionService.isPermissionExists(permission) ) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.permissionService.updatPermission(permission));
    }


    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<Void> deletePermission(@Valid @PathVariable("id") Long id) {
        Permission oldPermission = this.permissionService.getPermissionById(id);
        if( oldPermission == null ) {
            return ResponseEntity.badRequest().build();
        }
        this.permissionService.deletePermission(oldPermission);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/permissions")
    public ResponseEntity<?> getPermissions(Specification<Permission> spec , Pageable pageable) {
        return ResponseEntity.ok(this.permissionService.getPermissions(spec, pageable));
    }

    @GetMapping("/permissions/{id}")   
    public ResponseEntity<?> getPermissionById(@Valid @PathVariable("id") Long id) {
        Permission permission = this.permissionService.getPermissionById(id);
        if( permission == null ) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(permission);
    }

}
