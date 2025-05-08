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

import com.backend.domicare.dto.RoleDTO;
import com.backend.domicare.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Transactional
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Roles", description = "Endpoints for role management")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/roles")
    @Operation(summary = "Create new role", description = "Creates a new role in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Role created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleDTO role) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.createRole(role));
    }

    @PutMapping("/roles")
    @Operation(summary = "Update role", description = "Updates an existing role in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Role updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Role not found"),
        @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<RoleDTO> updateRole(@Valid @RequestBody RoleDTO role) {
        return ResponseEntity.ok(this.roleService.updateRole(role));
    }

    @DeleteMapping("/roles/{id}")
    @Operation(summary = "Delete role", description = "Deletes a role from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Role deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Role not found"),
        @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<Void> deleteRole(@Valid @PathVariable("id") Long id) {
        this.roleService.deleteRoleById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/roles")
    @Operation(summary = "Get all roles", description = "Returns all roles in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Roles retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<java.util.Set<RoleDTO>> getRoles() {
        return ResponseEntity.ok(this.roleService.getRoles());
    }

    @GetMapping("/roles/{id}")
    @Operation(summary = "Get role by ID", description = "Returns role details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Role details retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Role not found"),
        @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<RoleDTO> getRole(@Valid @PathVariable("id") Long id) {
        RoleDTO role = this.roleService.getRoleById(id);
        return ResponseEntity.ok(role);
    }
}
