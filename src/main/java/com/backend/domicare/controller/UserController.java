package com.backend.domicare.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.dto.paging.ResultPagingDTO;
import com.backend.domicare.dto.request.UpdateRoleForUserRequest;
import com.backend.domicare.dto.request.UpdateUserRequest;
import com.backend.domicare.model.User;
import com.backend.domicare.service.UserService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    @GetMapping("/users")
    public ResponseEntity<ResultPagingDTO> getUsers(
       @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection,
            @Filter Specification<User> spec, Pageable pageable) {

        if (sortBy != null && !sortBy.isEmpty()) {
        // Xử lý sắp xếp theo tham số sortBy và sortDirection
        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("desc") ? Sort.Order.desc(sortBy) : Sort.Order.asc(sortBy));
        pageable = PageRequest.of(page - 1, size, sort);
    } else {
        pageable = PageRequest.of(page - 1, size);
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
    public ResponseEntity<UserDTO> updateUser(@RequestBody UpdateUserRequest user) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.UpdateUserInformation(user));
    }

    @PutMapping("/users/avatar")
    public ResponseEntity<?> updateUserAvatar(@RequestParam("id") String id, @RequestBody MultipartFile avatar) {
        UserDTO userDTO = this.userService.updateUserAvatar(id, avatar);
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @PutMapping("/users/roles")
    public ResponseEntity<UserDTO> updateRoleForUser(@Valid @RequestBody UpdateRoleForUserRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.updateRoleForUser(request));
    }
    
}
