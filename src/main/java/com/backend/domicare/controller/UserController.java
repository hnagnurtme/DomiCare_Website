package com.backend.domicare.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.model.User;
import com.backend.domicare.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    @GetMapping("/users")
    public ResponseEntity<?> getUsers(
        @RequestParam(required = false) String search,
        @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        Specification<User> spec = (root, query, criteriaBuilder) -> {
            if (search != null && !search.isEmpty()) {
                return criteriaBuilder.like(root.get("name"), "%" + search + "%");
            }
            return criteriaBuilder.conjunction();
        };
        return ResponseEntity.ok(this.userService.getAllUsers(spec, pageable));
    }
}
