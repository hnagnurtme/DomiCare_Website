package com.backend.domicare.service.imp;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.backend.domicare.dto.FileDTO;
import com.backend.domicare.dto.RoleDTO;
import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.dto.paging.ResultPagingDTO;
import com.backend.domicare.dto.request.UpdateRoleForUserRequest;
import com.backend.domicare.dto.request.UpdateUserRequest;
import com.backend.domicare.exception.DeleteAdminException;
import com.backend.domicare.exception.EmailAlreadyExistException;
import com.backend.domicare.exception.NotFoundFileException;
import com.backend.domicare.exception.NotFoundRoleException;
import com.backend.domicare.exception.NotFoundUserException;
import com.backend.domicare.exception.NotMatchPasswordException;
import com.backend.domicare.mapper.UserMapper;
import com.backend.domicare.model.Role;
import com.backend.domicare.model.Token;
import com.backend.domicare.model.User;
import com.backend.domicare.repository.BookingsRepository;
import com.backend.domicare.repository.ReviewsRepository;
import com.backend.domicare.repository.TokensRepository;
import com.backend.domicare.repository.UsersRepository;
import com.backend.domicare.security.jwt.JwtTokenManager;
import com.backend.domicare.service.FileService;
import com.backend.domicare.service.RoleService;
import com.backend.domicare.service.UserService;
import com.backend.domicare.service.UserValidationService;
import com.backend.domicare.utils.ProjectConstants;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImp implements UserService {
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidationService userValidationService;
    private final RoleService roleService;
    private final FileService fileService;
    private final BookingsRepository bookingRepository;
    private final ReviewsRepository reviewRepository;
    private final TokensRepository tokenRepository;
    
    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        User user = UserMapper.INSTANCE.convertToUser(userDTO);
        if (userValidationService.isEmailAlreadyExist(user.getEmail())) {
            throw new EmailAlreadyExistException("Email already exists: " + user.getEmail());
        }
        Set<Role> roles = new HashSet<>();
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            for (RoleDTO role : userDTO.getRoles()) {
                Role managedRole = roleService.getRoleByName(role.getName());
                roles.add(managedRole);
            }
        } else {
            Role defaultRole = roleService.getRoleByName(ProjectConstants.DEFAULT_ROLE);
            roles.add(defaultRole);
        }
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return UserMapper.INSTANCE.convertToUserDTO(user);
    }

    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundUserException("User not found for email: " + email);
        }
        return user;
    }

    @Override
    public ResultPagingDTO getAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> users = this.userRepository.findAll(spec, pageable);

        List<UserDTO> userDTOs = UserMapper.INSTANCE.convertToUserDTOList(users.getContent());

        ResultPagingDTO result = new ResultPagingDTO();
        ResultPagingDTO.Meta meta = new ResultPagingDTO.Meta();
        meta.setPage(users.getNumber()+ 1);
        meta.setSize(users.getSize());
        meta.setTotal(users.getTotalElements());
        meta.setTotalPages(users.getTotalPages());
        result.setMeta(meta);
        result.setData(userDTOs);

        return result;
    }

    @Override
    public UserDTO findUserByEmailConfirmToken(String token) {
        Optional<User> user = userRepository.findByEmailConfirmationToken(token);
        if (user.isPresent()) {
            return UserMapper.INSTANCE.convertToUserDTO(user.get());
        }
        throw new NotFoundUserException("User not found for token: " + token);
    }

    @Override
    public String createVerificationToken(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundUserException("User not found for email: " + email);
        }
        String token = java.util.UUID.randomUUID().toString();
        user.setEmailConfirmationToken(token);
        userRepository.save(user);
        return token;
    }

    @Override
    public UserDTO updateConfirmedEmail(UserDTO user) {
        Long id = user.getId();
        User oldUser = userRepository.findUserById(id);
        if (oldUser == null) {
            throw new NotFoundUserException("User not found for id: " + id);
        }
        oldUser.setEmailConfirmed(user.isEmailConfirmed());
        oldUser.setEmailConfirmationToken(user.getEmailConfirmationToken());
        User userEntity = userRepository.save(oldUser);
        return UserMapper.INSTANCE.convertToUserDTO(userEntity);
    }

    @Override
    public boolean isEmailAlreadyExist(String email) {
        return userValidationService.isEmailAlreadyExist(email);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new NotFoundUserException("User not found for id: " + id);
        }
        return UserMapper.INSTANCE.convertToUserDTO(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundUserException("User not found for id: " + id));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> ProjectConstants.ROLE_ADMIN.equals(role.getName()));
        if (isAdmin) {
            throw new DeleteAdminException("Cannot delete an ADMIN account");
        }

        if (!CollectionUtils.isEmpty(user.getBookings())) {
            bookingRepository.deleteAll(user.getBookings());
        }
        if (!CollectionUtils.isEmpty(user.getReviews())) {
            reviewRepository.deleteAll(user.getReviews());
        }
        if (!CollectionUtils.isEmpty(user.getRefreshTokens())) {
            tokenRepository.deleteAll(user.getRefreshTokens());
        }
        userRepository.delete(user);
    }


    @Override
    public void resetPassword(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundUserException("User not found for email: " + email);
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public Token findByRefreshTokenWithUser(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken);
    }

    @Override
    public UserDTO updateUserInformation(UpdateUserRequest userRequest){
        Optional<String> currentUserLogin = JwtTokenManager.getCurrentUserLogin();
        User oldUser = null;
        if(currentUserLogin.isPresent()) {
            oldUser = userRepository.findByEmail(currentUserLogin.get());
        } else {
            throw new NotFoundUserException("User not found");
        }

        if (oldUser == null) {
            throw new NotFoundUserException("User not found for ");
        }
        if( userRequest.getName() != null){
            oldUser.setName(userRequest.getName());
        }
        if( userRequest.getPhone() != null){
            oldUser.setPhone(userRequest.getPhone());
        }
        if( userRequest.getAddress() != null){
            oldUser.setAddress(userRequest.getAddress());
        }
        if( userRequest.getDateOfBirth() != null){
            oldUser.setDateOfBirth(userRequest.getDateOfBirth());
        }
        if( userRequest.getGender() != null){
            oldUser.setGender(userRequest.getGender());
        }
        if( userRequest.getNewPassword() != null){
            if (userRequest.getOldPassword() == null) {
                throw new NotMatchPasswordException("Old password is required to set new password");
            }
            if (passwordEncoder.matches(userRequest.getOldPassword(), oldUser.getPassword())) {
                oldUser.setPassword(passwordEncoder.encode(userRequest.getNewPassword()));
            } else {
                throw new NotMatchPasswordException("Old password is incorrect");
            }
        }

        if (userRequest.getImageId() != null) {
            FileDTO fileDTO = fileService.fetchFileById(userRequest.getImageId());
            if (fileDTO == null) {
                throw new NotFoundFileException("File not found for id: " + userRequest.getImageId());
            }
            oldUser.setAvatar(fileDTO.getUrl());
        }
        return UserMapper.INSTANCE.convertToUserDTO(userRepository.save(oldUser));
    }

    @Override
    @Transactional
    public UserDTO updateRoleForUser(UpdateRoleForUserRequest request){
        Long userId = request.getUserId();
        List<Long> roleIds = request.getRoleIds();
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new NotFoundUserException("User not found for id: " + userId);
        }
        if (roleIds == null || roleIds.isEmpty()) {
            throw new NotFoundRoleException("Role not found for id: " + roleIds);
        }
        
        Set<Role> roles = new HashSet<>();
        for (Long roleId : roleIds) {
            Role role = roleService.getRoleEntityById(roleId);
            if (role == null) {
                throw new NotFoundRoleException("Role not found for id: " + roleId);
            }
            roles.add(role);
        }
        
        user.setRoles(roles);
        userRepository.save(user);
        return UserMapper.INSTANCE.convertToUserDTO(user);
    }

    @Override
    public void deleteRefreshTokenByUserId(Long id){
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new NotFoundUserException("User not found for id: " + id);
        }
        List<Token> tokens = tokenRepository.findByUserId(id);
        if (tokens != null && !tokens.isEmpty()) {
            tokenRepository.deleteAll(tokens);
        }
    }
}