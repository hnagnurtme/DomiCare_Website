package com.backend.domicare.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.dto.paging.ResultPagingDTO;
import com.backend.domicare.model.User;
import com.backend.domicare.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    @GetMapping("/users")
    public ResponseEntity<ResultPagingDTO> getUsers(
        @RequestParam(required = false) String search,
        @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
    Specification<User> spec = Specification.where(null);

    if (search != null && !search.isBlank()) {
        String[] criteria = search.split(",");

        for (String criterion : criteria) {
            String[] parts = criterion.split(":|<|>");

            if (parts.length == 2) {
                String key = parts[0];
                String value = parts[1];

                if (criterion.contains(":")) {
                    spec = spec.and((root, query, cb) -> cb.like(root.get(key), "%" + value + "%"));
                } else if (criterion.contains(">")) {
                    spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get(key), value));
                } else if (criterion.contains("<")) {
                    spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get(key), value));
                }
            }
        }
    }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAllUsers(spec, pageable));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id) {
        UserDTO user = this.userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Long id) {
        this.userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/users")
    public ResponseEntity<UserDTO> updateUser( @RequestBody UserDTO user) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.updateUser(user));
    }

    @PostMapping("/users/avatar")
    public ResponseEntity<UserDTO> updateUserAvatar(@RequestParam("id") String id, @RequestBody MultipartFile avatar) {
        this.userService.updateUserAvatar(id, avatar);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    

}
