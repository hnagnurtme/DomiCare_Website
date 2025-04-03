package com.backend.domicare.service.imp;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.backend.domicare.dto.FileDTO;
import com.backend.domicare.dto.RoleDTO;
import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.dto.paging.ResultPagingDTO;
import com.backend.domicare.dto.request.UpdateUserRequest;
import com.backend.domicare.exception.DeleteAdminException;
import com.backend.domicare.exception.EmailAlreadyExistException;
import com.backend.domicare.exception.NotFoundException;
import com.backend.domicare.exception.NotFoundUserException;
import com.backend.domicare.mapper.UserMapper;
import com.backend.domicare.model.Role;
import com.backend.domicare.model.Token;
import com.backend.domicare.model.User;
import com.backend.domicare.repository.BookingsRepository;
import com.backend.domicare.repository.ReviewsRepository;
import com.backend.domicare.repository.RolesRepository;
import com.backend.domicare.repository.TokensRepository;
import com.backend.domicare.repository.UsersRepository;
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
    private final RolesRepository roleRepository;

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
            throw new NotFoundException("User not found for email: " + email);
        }
        return user;
    }

    @Override
    public ResultPagingDTO getAllUsers(Specification<User> spec, Pageable pageable) {
        Page<User> users = this.userRepository.findAll(spec, pageable);
        ResultPagingDTO resultPaginDTO = new ResultPagingDTO();
        ResultPagingDTO.Meta meta = new ResultPagingDTO.Meta();

        meta.setPage(users.getNumber());
        meta.setSize(users.getSize());
        meta.setTotal(users.getTotalElements());
        meta.setTotalPages(users.getTotalPages());

        resultPaginDTO.setMeta(meta);
        resultPaginDTO.setData(users.getContent());
        return resultPaginDTO;
    }

    @Override
    public UserDTO findUserByEmailConfirmToken(String token) {
        Optional<User> user = userRepository.findByEmailConfirmationToken(token);
        if (user.isPresent()) {
            return UserMapper.INSTANCE.convertToUserDTO(user.get());
        }
        throw new NotFoundException("User not found for token: " + token);
    }

    @Override
    public String createVerificationToken(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("User not found for email: " + email);
        }
        String token = java.util.UUID.randomUUID().toString();
        user.setEmailConfirmationToken(token);
        userRepository.save(user);
        return token;
    }

    @Override
    public UserDTO updateUserInfo(UserDTO user) {
        Long id = user.getId();
        User oldUser = userRepository.findUserById(id);
        if (oldUser == null) {
            throw new NotFoundUserException("User not found for id: " + id);
        }
        oldUser.setName(user.getName());
        oldUser.setEmail(user.getEmail());
        oldUser.setPhone(user.getPhone());
        oldUser.setAddress(user.getAddress());
        oldUser.setEmailConfirmed(user.isEmailConfirmed());
        oldUser.setEmailConfirmationToken(user.getEmailConfirmationToken());
        oldUser.setAvatar(user.getAvatar());

        Set<Role> roles = new HashSet<>();
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            for (RoleDTO role : user.getRoles()) {
                Role managedRole = roleService.getRoleByName(role.getName());
                roles.add(managedRole);
            }
        } else {
            Role defaultRole = roleService.getRoleByName(ProjectConstants.DEFAULT_ROLE);
            roles.add(defaultRole);
        }
        oldUser.setRoles(roles);
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
            throw new NotFoundException("User not found for id: " + id);
        }
        return UserMapper.INSTANCE.convertToUserDTO(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found for id: " + id));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> ProjectConstants.ROLE_ADMIN.equals(role.getName()));
        if (isAdmin) {
            throw new DeleteAdminException("Cannot delete an ADMIN account");
        }

        userRepository.deleteAllRolesByUserID(id);
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
    public UserDTO updateUser(UpdateUserRequest userUpdate) {
        UserDTO user = UserMapper.INSTANCE.convertToUserDTO(userUpdate);
        User oldUser = userRepository.findUserById(user.getId());
        if (oldUser == null) {
            throw new NotFoundException("User not found for id: " + user.getId());
        }

        Set<Role> roles = new HashSet<>();
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            for (RoleDTO role : user.getRoles()) {
                Role managedRole = roleService.getRoleByName(role.getName());
                roles.add(managedRole);
            }
        } else {
            Role defaultRole = roleService.getRoleByName(ProjectConstants.DEFAULT_ROLE);
            roles.add(defaultRole);
        }
        oldUser.setRoles(roles);

        String password = oldUser.getPassword();
        if (user.getPassword() != null) {
            oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            oldUser.setPassword(password);
        }

        oldUser.setName(user.getName());
        oldUser.setEmail(user.getEmail());
        oldUser.setPhone(user.getPhone());
        oldUser.setAddress(user.getAddress());

        User newUser = userRepository.save(oldUser);
        return UserMapper.INSTANCE.convertToUserDTO(newUser);
    }

    @Override
    public void resetPassword(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("User not found for email: " + email);
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public Token findByRefreshTokenWithUser(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken);
    }

    @Override
    public String updateUserAvatar(String id, MultipartFile avatar) {
        try {
            Long userId = Long.valueOf(id);
            User user = userRepository.findUserById(userId);
            if (user == null) {
                throw new NotFoundException("User not found for id: " + id);
            }
            String fileName = user.getEmail();
            FileDTO fileDTO = fileService.uploadFile(avatar, fileName, true);
            if (fileDTO == null || fileDTO.getUrl() == null) {
                throw new NotFoundException("Failed to upload avatar for id: " + id);
            }
            user.setAvatar(fileDTO.getUrl());
            userRepository.save(user);
            return fileName;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid user id: " + id);
        }
    }

    @Override
    public void deleteRefreshToken(String refreshToken) {
        Token token = tokenRepository.findByRefreshToken(refreshToken);
        if (token == null) {
            throw new NotFoundException("Token not found: " + refreshToken);
        }
        tokenRepository.delete(token);
    }
}