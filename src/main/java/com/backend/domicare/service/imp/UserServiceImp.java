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
import com.backend.domicare.dto.request.AddUserByAdminRequest;
import com.backend.domicare.dto.request.UpdateRoleForUserRequest;
import com.backend.domicare.dto.request.UpdateUserRequest;
import com.backend.domicare.dto.response.SalePagingResponse;
import com.backend.domicare.dto.response.UserPagingResponse;
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
import com.backend.domicare.utils.FormatStringAccents;
import com.backend.domicare.utils.ProjectConstants;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.converters.models.Sort;
@RequiredArgsConstructor
@Service
public class UserServiceImp implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImp.class);

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
        logger.info("Creating new user with email: {}", userDTO.getEmail());
        User user = UserMapper.INSTANCE.convertToUser(userDTO);
        if (userValidationService.isEmailAlreadyExist(user.getEmail())) {
            logger.warn("Email already exists: {}", user.getEmail());
            throw new EmailAlreadyExistException("Email already exists: " + user.getEmail());
        }
        Set<Role> roles = new HashSet<>();
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            for (RoleDTO role : userDTO.getRoles()) {
                Role managedRole = roleService.getRoleByName(role.getName());
                roles.add(managedRole);
            }
            logger.debug("Assigned custom roles to user");
        } else {
            Role defaultRole = roleService.getRoleByName(ProjectConstants.DEFAULT_ROLE);
            roles.add(defaultRole);
            logger.debug("Assigned default role to user");
        }
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmailConfirmed(userDTO.isEmailConfirmed());
        user.setEmailConfirmationToken(userDTO.getEmailConfirmationToken());
        user.setAvatar(userDTO.getAvatar());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setGender(userDTO.getGender());
        user.setName(userDTO.getName());
        user.setActive(userDTO.getIsActive());
        user.setDeleted(false);
        user.setNameUnsigned(FormatStringAccents.removeTones(user.getName()));
        User createdUser = userRepository.save(user);
        logger.info("User created successfully with ID: {}", createdUser.getId());
        return UserMapper.INSTANCE.convertToUserDTO(createdUser);
    }

    @Override
    public User findUserByEmail(String email) {
        logger.debug("Finding user by email: {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.warn("User not found for email: {}", email);
            throw new NotFoundUserException("User not found for email: " + email);
        }
        logger.debug("User found with ID: {}", user.getId());
        return user;
    }

    @Override
    public ResultPagingDTO getAllUsers(Specification<User> spec, Pageable pageable) {
        logger.debug("Fetching users with pagination [page: {}, size: {}]",
                pageable.getPageNumber(), pageable.getPageSize());
        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isDeleted"), false));

        Page<User> users = this.userRepository.findAll(spec, pageable);

        List<UserPagingResponse> userDTOs = UserMapper.INSTANCE.convertToUserPagingResponseList(users.getContent());
    

        ResultPagingDTO result = new ResultPagingDTO();
        ResultPagingDTO.Meta meta = new ResultPagingDTO.Meta();
        meta.setPage(users.getNumber() + 1);
        meta.setSize(users.getSize());
        meta.setTotal(users.getTotalElements());
        meta.setTotalPages(users.getTotalPages());
        result.setMeta(meta);
        result.setData(userDTOs);

        logger.debug("Found {} users out of {} total", userDTOs.size(), users.getTotalElements());
        return result;
    }

    @Override
    public ResultPagingDTO getAllCustomer(Specification<User> spec, Pageable pageable) {
        logger.debug("Fetching customers with pagination [page: {}, size: {}]",
                pageable.getPageNumber(), pageable.getPageSize());

        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder
                    .like(root.join("roles").get("name"), "%" + ProjectConstants.ROLE_USER + "%"));
        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isDeleted"), false));


        Page<User> users = this.userRepository.findAll(spec, pageable);

        List<UserPagingResponse> userDTOs = UserMapper.INSTANCE.convertToUserPagingResponseList(users.getContent());

        ResultPagingDTO result = new ResultPagingDTO();
        ResultPagingDTO.Meta meta = new ResultPagingDTO.Meta();
        meta.setPage(users.getNumber() + 1);
        meta.setSize(users.getSize());
        meta.setTotal(users.getTotalElements());
        meta.setTotalPages(users.getTotalPages());
        result.setMeta(meta);
        result.setData(userDTOs);

        logger.debug("Found {} customers out of {} total", userDTOs.size(), users.getTotalElements());
        return result;
    }

    @Override
    public ResultPagingDTO getAllSale(Specification<User> spec, Pageable pageable) {
        logger.debug("Fetching sales with pagination [page: {}, size: {}]",
                pageable.getPageNumber(), pageable.getPageSize());
        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder
                    .like(root.join("roles").get("name"), "%" + ProjectConstants.ROLE_SALE + "%"));
        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isDeleted"), false));
        
        Page<User> users = this.userRepository.findAll(spec, pageable);
        

        List<SalePagingResponse> userDTOs = UserMapper.INSTANCE.convertToSalePagingResponseList(users.getContent());

        ResultPagingDTO result = new ResultPagingDTO();
        ResultPagingDTO.Meta meta = new ResultPagingDTO.Meta();
        meta.setPage(users.getNumber() + 1);
        meta.setSize(users.getSize());
        meta.setTotal(users.getTotalElements());
        meta.setTotalPages(users.getTotalPages());
        result.setMeta(meta);
        result.setData(userDTOs);

        logger.debug("Found {} sales out of {} total", userDTOs.size(), users.getTotalElements());
        return result;
    }

    @Override
    public UserDTO findUserByEmailConfirmToken(String token) {
        logger.debug("Finding user by email confirmation token");
        Optional<User> user = userRepository.findByEmailConfirmationToken(token);
        if (user.isPresent()) {
            logger.debug("User found with ID: {}", user.get().getId());
            return UserMapper.INSTANCE.convertToUserDTO(user.get());
        }
        logger.warn("No user found with the provided token");
        throw new NotFoundUserException("User not found for token: " + token);
    }

    @Override
    public String createVerificationToken(String email) {
        logger.debug("Creating verification token for email: {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.warn("Failed to create verification token - user not found for email: {}", email);
            throw new NotFoundUserException("User not found for email: " + email);
        }
        String token = java.util.UUID.randomUUID().toString();
        user.setEmailConfirmationToken(token);
        userRepository.save(user);
        logger.debug("Verification token created successfully for user ID: {}", user.getId());
        return token;
    }

    @Override
    public UserDTO updateConfirmedEmail(UserDTO user) {
        Long id = user.getId();
        logger.debug("Updating email confirmation status for user ID: {}", id);

        User oldUser = userRepository.findUserById(id);
        if (oldUser == null) {
            logger.warn("Failed to update email confirmation - user not found with ID: {}", id);
            throw new NotFoundUserException("User not found for id: " + id);
        }

        oldUser.setEmailConfirmed(user.isEmailConfirmed());
        oldUser.setEmailConfirmationToken(user.getEmailConfirmationToken());

        User userEntity = userRepository.save(oldUser);
        logger.info("Email confirmation status updated for user ID: {}, confirmed: {}",
                userEntity.getId(), userEntity.isEmailConfirmed());

        return UserMapper.INSTANCE.convertToUserDTO(userEntity);
    }

    @Override
    public boolean isEmailAlreadyExist(String email) {
        return userValidationService.isEmailAlreadyExist(email);
    }

    @Override
    public UserDTO getUserById(Long id) {
        logger.debug("Fetching user by ID: {}", id);
        User user = userRepository.findUserById(id);
        if (user == null) {
            logger.warn("User not found with ID: {}", id);
            throw new NotFoundUserException("User not found for id: " + id);
        }
        logger.debug("User found with ID: {}", id);
        return UserMapper.INSTANCE.convertToUserDTO(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        logger.info("Attempting to delete user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Delete failed - user not found with ID: {}", id);
                    return new NotFoundUserException("User not found for id: " + id);
                });

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> ProjectConstants.ROLE_ADMIN.equals(role.getName()));
        if (isAdmin) {
            logger.warn("Cannot delete admin user with ID: {}", id);
            throw new DeleteAdminException("Cannot delete an ADMIN account");
        }

        int bookingsCount = 0, reviewsCount = 0, tokensCount = 0;

        if (!CollectionUtils.isEmpty(user.getBookings())) {
            bookingsCount = user.getBookings().size();
            logger.debug("Deleting {} bookings for user ID: {}", bookingsCount, id);
            bookingRepository.deleteAll(user.getBookings());
        }

        if (!CollectionUtils.isEmpty(user.getReviews())) {
            reviewsCount = user.getReviews().size();
            logger.debug("Deleting {} reviews for user ID: {}", reviewsCount, id);
            reviewRepository.deleteAll(user.getReviews());
        }

        if (!CollectionUtils.isEmpty(user.getRefreshTokens())) {
            tokensCount = user.getRefreshTokens().size();
            logger.debug("Deleting {} refresh tokens for user ID: {}", tokensCount, id);
            tokenRepository.deleteAll(user.getRefreshTokens());
        }

        userRepository.softDeleteById(id);
        logger.info("User deleted successfully with ID: {} (removed {} bookings, {} reviews, {} tokens)",
                id, bookingsCount, reviewsCount, tokensCount);
    }

    @Override
    public void resetPassword(String email, String password) {
        logger.info("Attempting to reset password for user with email: {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.warn("Password reset failed - user not found with email: {}", email);
            throw new NotFoundUserException("User not found for email: " + email);
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        logger.info("Password reset successfully for user ID: {}", user.getId());
    }

    @Override
    public Token findByRefreshTokenWithUser(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken);
    }

    @Override
    public UserDTO updateUserInformation(UpdateUserRequest userRequest) {
        logger.info("Updating user information");

        Optional<String> currentUserLogin = JwtTokenManager.getCurrentUserLogin();
        User oldUser = null;
        if (currentUserLogin.isPresent()) {
            String email = currentUserLogin.get();
            logger.debug("Fetching user with email: {}", email);
            oldUser = userRepository.findByEmail(email);
        } else {
            logger.warn("Update failed - no authenticated user found");
            throw new NotFoundUserException("No authenticated user found");
        }

        if (oldUser == null) {
            logger.warn("Update failed - user not found with current login");
            throw new NotFoundUserException("User not found for current login");
        }

        Long userId = oldUser.getId();
        logger.debug("Updating information for user ID: {}", userId);

        boolean hasChanges = false;

        if (userRequest.getName() != null && !userRequest.getName().equals(oldUser.getName())) {
            oldUser.setName(userRequest.getName());
            oldUser.setNameUnsigned(FormatStringAccents.removeTones(userRequest.getName()));
            logger.debug("Updated name for user ID: {}", userId);
            hasChanges = true;
        }

        if (userRequest.getPhone() != null && !userRequest.getPhone().equals(oldUser.getPhone())) {
            oldUser.setPhone(userRequest.getPhone());
            logger.debug("Updated phone for user ID: {}", userId);
            hasChanges = true;
        }

        if (userRequest.getAddress() != null && !userRequest.getAddress().equals(oldUser.getAddress())) {
            oldUser.setAddress(userRequest.getAddress());
            logger.debug("Updated address for user ID: {}", userId);
            hasChanges = true;
        }

        if (userRequest.getDateOfBirth() != null && !userRequest.getDateOfBirth().equals(oldUser.getDateOfBirth())) {
            oldUser.setDateOfBirth(userRequest.getDateOfBirth());
            logger.debug("Updated date of birth for user ID: {}", userId);
            hasChanges = true;
        }

        if (userRequest.getGender() != null && !userRequest.getGender().equals(oldUser.getGender())) {
            oldUser.setGender(userRequest.getGender());
            logger.debug("Updated gender for user ID: {}", userId);
            hasChanges = true;
        }

        if (userRequest.getNewPassword() != null) {
            if (userRequest.getOldPassword() == null) {
                logger.warn("Password update failed - old password not provided for user ID: {}", userId);
                throw new NotMatchPasswordException("Old password is required to set new password");
            }

            if (passwordEncoder.matches(userRequest.getOldPassword(), oldUser.getPassword())) {
                oldUser.setPassword(passwordEncoder.encode(userRequest.getNewPassword()));
                logger.debug("Updated password for user ID: {}", userId);
                hasChanges = true;
            } else {
                logger.warn("Password update failed - incorrect old password for user ID: {}", userId);
                throw new NotMatchPasswordException("Old password is incorrect");
            }
        }

        if (userRequest.getImageId() != null) {
            FileDTO fileDTO = fileService.fetchFileById(userRequest.getImageId());
            if (fileDTO == null) {
                logger.warn("Avatar update failed - file not found with ID: {}", userRequest.getImageId());
                throw new NotFoundFileException("File not found for id: " + userRequest.getImageId());
            }
            oldUser.setAvatar(fileDTO.getUrl());
            logger.debug("Updated avatar for user ID: {} with file ID: {}", userId, userRequest.getImageId());
            hasChanges = true;
        }

        User savedUser = userRepository.save(oldUser);
        logger.info("User information {} for user ID: {}", hasChanges ? "updated" : "unchanged", userId);
        return UserMapper.INSTANCE.convertToUserDTO(savedUser);
    }

    @Override
    @Transactional
    public UserDTO updateRoleForUser(UpdateRoleForUserRequest request) {
        Long userId = request.getUserId();
        List<Long> roleIds = request.getRoleIds();

        logger.info("Updating roles for user ID: {}", userId);

        User user = userRepository.findUserById(userId);
        if (user == null) {
            logger.warn("Role update failed - user not found with ID: {}", userId);
            throw new NotFoundUserException("User not found for id: " + userId);
        }

        if (roleIds == null || roleIds.isEmpty()) {
            logger.warn("Role update failed - no roles provided for user ID: {}", userId);
            throw new NotFoundRoleException("Role IDs cannot be empty");
        }

        Set<Role> roles = new HashSet<>();
        for (Long roleId : roleIds) {
            Role role = roleService.getRoleEntityById(roleId);
            if (role == null) {
                logger.warn("Role update failed - role not found with ID: {}", roleId);
                throw new NotFoundRoleException("Role not found for id: " + roleId);
            }
            roles.add(role);
            logger.debug("Adding role '{}' to user ID: {}", role.getName(), userId);
        }

        Set<String> originalRoleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(java.util.stream.Collectors.toSet());
        Set<String> newRoleNames = roles.stream()
                .map(Role::getName)
                .collect(java.util.stream.Collectors.toSet());

        user.setRoles(roles);
        User updatedUser = userRepository.save(user);

        logger.info("Roles updated for user ID: {}, changed from {} to {}",
                userId, originalRoleNames, newRoleNames);

        return UserMapper.INSTANCE.convertToUserDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deleteRefreshTokenByUserId(Long id) {
        logger.debug("Attempting to delete refresh tokens for user ID: {}", id);

        if (id == null) {
            logger.warn("Token deletion failed - user ID is null");
            throw new IllegalArgumentException("User ID cannot be null");
        }

        boolean userExists = userRepository.existsById(id);
        if (!userExists) {
            logger.warn("Token deletion failed - user not found with ID: {}", id);
            throw new NotFoundUserException("User not found for id: " + id);
        }
        try {
            List<Token> tokens = tokenRepository.findByUserId(id);
            if (tokens != null && !tokens.isEmpty()) {
                tokenRepository.deleteAll(tokens);
                logger.debug("Deleted {} refresh tokens for user ID: {}", tokens.size(), id);
            } else {
                logger.debug("No refresh tokens found for user ID: {}", id);
            }
            logger.info("Successfully deleted all refresh tokens for user ID: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting refresh tokens for user ID: {}", id, e);
            throw new RuntimeException("Failed to delete refresh tokens", e);
        }
    }

    @Override
    public UserDTO addUserByAdmin(AddUserByAdminRequest user) {
        logger.info("Adding new user by admin with email: {}", user.getEmail());

        UserDTO userDTO = UserMapper.INSTANCE.convertToUserDTOByAddUserRequest(user);

        userDTO.setEmailConfirmationToken(null);
        userDTO.setEmailConfirmed(true);
        userDTO.setIsActive(true);

        Set<RoleDTO> roles = new HashSet<>();
        RoleDTO role = this.roleService.getRoleById(user.getRoleId());
        if (role == null) {
            logger.warn("User creation by admin failed - role not found with ID: {}", user.getRoleId());
            throw new NotFoundRoleException("Role not found for id: " + user.getRoleId());
        }

        roles.add(role);
        userDTO.setRoles(roles);
        logger.debug("Assigning role '{}' to new user", role.getName());

        UserDTO createdUser = saveUser(userDTO);
        logger.info("User created successfully by admin with ID: {}", createdUser.getId());

        return createdUser;
    }


}