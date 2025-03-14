package com.backend.domicare.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.dto.paging.ResultPagingDTO;


import com.backend.domicare.model.User;

public interface UserService {
    public UserDTO saveUser(UserDTO user) ;

    public User findUserByEmail(String email) ;
    
    public ResultPagingDTO getAllUsers(Specification<User> spec,Pageable pageable);
    public UserDTO findUserByEmailConfirmToken(String token);
    public String createVerificationToken(String email);
    public UserDTO updateUserInfo(UserDTO user);
    public boolean isEmailAlreadyExist(String email);
}
