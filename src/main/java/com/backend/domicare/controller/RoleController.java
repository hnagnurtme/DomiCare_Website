package com.backend.domicare.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domicare.model.Role;
import com.backend.domicare.service.RoleService;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.createRole(role));
    }

    @PutMapping("/roles")
    public ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) {
        return ResponseEntity.ok(this.roleService.updateRole(role));
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@Valid @PathVariable("id") Long id) {
        this.roleService.deleteRoleById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {
        return ResponseEntity.ok(this.roleService.getRoles());
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<?> getRole(@Valid @PathVariable("id") Long id) {
        Role role = this.roleService.getRoleById(id);
        return ResponseEntity.ok(role);
    }
}
