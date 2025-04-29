package com.backend.domicare.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domicare.dto.RoleDTO;
import com.backend.domicare.service.RoleService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Transactional
@SecurityRequirement(name = "bearerAuth")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/roles")
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleDTO role) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.createRole(role));
    }

    @PutMapping("/roles")
    public ResponseEntity<RoleDTO> updateRole(@Valid @RequestBody RoleDTO role) {
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
        RoleDTO role = this.roleService.getRoleById(id);
        return ResponseEntity.ok(role);
    }
}
