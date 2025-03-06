package com.backend.domicare.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.dto.paging.ResultPaginDTO;
import com.backend.domicare.dto.request.UpdateUserRequest;
import com.backend.domicare.dto.response.UpdateUserResponse;

import com.backend.domicare.model.User;

public interface UserService {
    // public User findByEmail(String email);
    // public void deleteUserById(Long id);
    public User saveUser(UserDTO user);
    // public User findUserById(Long id);
    // public UpdateUserResponse updateUser(Long id, UpdateUserRequest updateUserRequest);
    public User findUserByEmail(String email);
    public ResultPaginDTO getAllUsers(Specification<User> spec,Pageable pageable);
}
