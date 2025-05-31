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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.dto.paging.ResultPagingDTO;
import com.backend.domicare.dto.request.AddUserByAdminRequest;
import com.backend.domicare.dto.request.UpdateRoleForUserRequest;
import com.backend.domicare.dto.request.UpdateUserRequest;
import com.backend.domicare.model.User;
import com.backend.domicare.service.UserService;
import com.backend.domicare.utils.FormatStringAccents;
import com.turkraft.springfilter.boot.Filter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Users", description = "Endpoints for user management")
public class UserController {
        private final UserService userService;

        @GetMapping("/users")
        @Operation(summary = "Get all users with optional filtering", description = "Returns paginated list of users with optional filtering by name or role")
        @Parameter(name = "page", description = "Page number (default 1)", example = "1")
        @Parameter(name = "size", description = "Page size (default 20)", example = "10")
        @Parameter(name = "searchName", description = "Filter by user name", example = "Trung Ánh")
        @Parameter(name = "searchRoleName", description = "Filter by role name", example = "ROLE_SALE")
        @Parameter(name = "sortBy", description = "Sort field (camelCase field in User)", example = "userTotalSuccessBookings")
        @Parameter(name = "sortDirection", description = "Sort direction", example = "desc")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Users retrieved successfully", content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "SuccessResponse", value = """
                                        {
                                          "status": 200,
                                          "error": null,
                                          "message": "Success",
                                          "data": {
                                            "meta": {
                                              "page": 1,
                                              "size": 1,
                                              "total": 1,
                                              "totalPages": 1
                                            },
                                            "data": [
                                              {
                                                "id": 1,
                                                "name": "Nguyễn Trung Ánh",
                                                "email": "anhnon0106@gmail.com",
                                                "phone": "0768406235",
                                                "address": "Âu Cơ - Hoà Khánh Bắc - Đà Nẵng",
                                                "avatar": "https://...",
                                                "gender": "MALE",
                                                "isActive": true,
                                                "dateOfBirth": "2005-06-14T17:00:00Z",
                                                "userTotalSuccessBookings": 0,
                                                "userTotalFailedBookings": 0,
                                                "saleTotalBookings": 0,
                                                "saleSuccessPercent": 0
                                              }
                                            ]
                                          }
                                        }
                                        """))),
                        @ApiResponse(responseCode = "403", description = "Unauthorized access")
        })
        public ResponseEntity<ResultPagingDTO> getUsers(
                        @RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "20") int size,
                        @RequestParam(required = false) String searchName,
                        @RequestParam(required = false) String searchRoleName,
                        @RequestParam(required = false, defaultValue = "id") String sortBy,
                        @RequestParam(required = false, defaultValue = "asc") String sortDirection,
                        @Filter Specification<User> spec, Pageable pageable) {

                Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                                : Sort.by(sortBy).ascending();
                pageable = PageRequest.of(page - 1, size, sort);

                if (searchName != null && !searchName.isEmpty()) {

                        String cleanSearchName = FormatStringAccents.removeTones(searchName.toLowerCase());

                        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder
                                        .like(criteriaBuilder.lower(root.get("nameUnsigned")),
                                                        "%" + cleanSearchName + "%"));
                }

                if (searchRoleName != null && !searchRoleName.isEmpty()) {
                        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder
                                        .like(root.join("roles").get("name"), "%" + searchRoleName + "%"));
                }

                return ResponseEntity.ok(this.userService.getAllUsers(spec, pageable));
        }

        @GetMapping("/users/{id}")
        @Operation(summary = "Get user by ID", description = "Returns user details by ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User details retrieved successfully"),
                        @ApiResponse(responseCode = "404", description = "User not found"),
                        @ApiResponse(responseCode = "403", description = "Unauthorized access")
        })
        public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id) {
                UserDTO user = this.userService.getUserById(id);
                return ResponseEntity.ok(user);
        }

        @DeleteMapping("/users/{id}")
        @Operation(summary = "Delete user by ID", description = "Removes a user from the system")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
                        @ApiResponse(responseCode = "404", description = "User not found"),
                        @ApiResponse(responseCode = "403", description = "Unauthorized access")
        })
        public ResponseEntity<Void> deleteUserById(@PathVariable("id") Long id) {
                this.userService.deleteUserById(id);
                return ResponseEntity.noContent().build();
        }

        @PutMapping("/users")
        @Operation(summary = "Update user information", description = "Updates the specified user information")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User updated successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid input"),
                        @ApiResponse(responseCode = "403", description = "Unauthorized access"),
                        @ApiResponse(responseCode = "404", description = "User not found")
        })
        public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UpdateUserRequest user) {
                return ResponseEntity.ok(this.userService.updateUserInformation(user));
        }

        @PutMapping("/users/roles")
        @Operation(summary = "Update user roles", description = "Updates the roles assigned to a user")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User roles updated successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid input"),
                        @ApiResponse(responseCode = "403", description = "Unauthorized access"),
                        @ApiResponse(responseCode = "404", description = "User or role not found")
        })
        public ResponseEntity<UserDTO> updateRoleForUser(@Valid @RequestBody UpdateRoleForUserRequest request) {
                return ResponseEntity.ok(this.userService.updateRoleForUser(request));
        }

        @PostMapping("/users")
        @Operation(summary = "Create new user", description = "Creates a new user account by administrator")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "User created successfully"),
                        @ApiResponse(responseCode = "400", description = "Invalid input"),
                        @ApiResponse(responseCode = "403", description = "Unauthorized access"),
                        @ApiResponse(responseCode = "409", description = "User already exists")
        })
        public ResponseEntity<UserDTO> createUserByAdmin(@Valid @RequestBody AddUserByAdminRequest user) {
                return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.addUserByAdmin(user));
        }
}
