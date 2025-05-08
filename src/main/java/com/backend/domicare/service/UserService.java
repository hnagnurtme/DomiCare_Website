package com.backend.domicare.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.dto.paging.ResultPagingDTO;
import com.backend.domicare.dto.request.AddUserByAdminRequest;
import com.backend.domicare.dto.request.UpdateRoleForUserRequest;
import com.backend.domicare.dto.request.UpdateUserRequest;
import com.backend.domicare.model.Token;
import com.backend.domicare.model.User;

public interface UserService {
    public UserDTO saveUser(UserDTO user ) ;

    public User findUserByEmail(String email) ;
    
    public ResultPagingDTO getAllUsers(Specification<User> spec,Pageable pageable);

    public UserDTO updateConfirmedEmail(UserDTO user);

    public UserDTO findUserByEmailConfirmToken(String token);

    public String createVerificationToken(String email);

    public boolean isEmailAlreadyExist(String email);

    public UserDTO getUserById(Long id);

    public void deleteUserById(Long id);

    public void resetPassword(String email, String password);

    public Token findByRefreshTokenWithUser(String refreshToken);

    public void deleteRefreshTokenByUserId(Long id);

    public UserDTO updateUserInformation(UpdateUserRequest user);

    public UserDTO updateRoleForUser(UpdateRoleForUserRequest user);

    public UserDTO addUserByAdmin(AddUserByAdminRequest user);

}
