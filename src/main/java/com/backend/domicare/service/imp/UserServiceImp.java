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

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.dto.paging.ResultPagingDTO;
import com.backend.domicare.exception.DeleteAdminException;
import com.backend.domicare.exception.NotFoundException;
import com.backend.domicare.mapper.UserMapper;
import com.backend.domicare.model.Role;
import com.backend.domicare.model.User;
import com.backend.domicare.repository.BookingsRepository;
import com.backend.domicare.repository.ReviewsRepository;
import com.backend.domicare.repository.TokensRepository;
import com.backend.domicare.repository.UsersRepository;
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

    private final BookingsRepository bookingRepository;


    private final ReviewsRepository reviewRepository;


    private final TokensRepository tokenRepository;



    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        User user = UserMapper.INSTANCE.convertToUser(userDTO);
        if (userValidationService.isEmailAlreadyExist(user.getEmail())) {
            throw new NotFoundException("Email already exists");
        }
        Set<Role> roles = new HashSet<>();
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            for (Role role : userDTO.getRoles()) {
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
    public User findUserByEmail(String email)  {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("Email not found : " + email);
        }
        return user;
    }

    @Override
    public ResultPagingDTO getAllUsers(Specification<User> spec,Pageable pageable) {
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
    public UserDTO findUserByEmailConfirmToken(String token){
        Optional<User> user = userRepository.findByEmailConfirmationToken(token);
        if(user.isPresent()){
            return UserMapper.INSTANCE.convertToUserDTO(user.get());
        }
        throw new NotFoundException(" Not found user for token : " + token);
    }

    @Override
    public String createVerificationToken(String email){
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new NotFoundException("Not found user for : " + email);
        }
        String token = java.util.UUID.randomUUID().toString();
        user.setEmailConfirmationToken(token);
        userRepository.save(user);
        return token;
    }

    @Override
    public UserDTO updateUserInfo(UserDTO user){
        User oldUser = userRepository.findByEmail(user.getEmail());
        if(oldUser == null){
            throw new NotFoundException("Not found user for : " + user.getEmail());
        }
        User userMapper= UserMapper.INSTANCE.convertToUser(user);
        User newUser = userRepository.save(userMapper);
        return UserMapper.INSTANCE.convertToUserDTO(newUser);
    }
    @Override
    public boolean isEmailAlreadyExist(String email){
        return userValidationService.isEmailAlreadyExist(email);
    }

    @Override
    public UserDTO getUserById(Long id){
        User user = userRepository.findUserById(id);
        if(user == null){
            throw new NotFoundException("Not found user for id : " + id);
        }
        return UserMapper.INSTANCE.convertToUserDTO(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User not found with id " + id));
    
        // Kiểm tra User là ADMIN không cho phép xóa
        boolean isAdmin = user.getRoles().stream()
            .anyMatch(role -> ProjectConstants.ROLE_ADMIN.equals(role.getName()));
    
        if (isAdmin) {
            throw new DeleteAdminException("Cannot delete admin account");
        }
    
        // Xóa liên kết bảng trung gian
        userRepository.deleteRolesByUserId(id);
    
        // Xóa dữ liệu liên quan nếu tồn tại (Bookings, Reviews, Tokens)
        if (!CollectionUtils.isEmpty(user.getBookings())) {
            bookingRepository.deleteAll(user.getBookings());
        }
    
        if (!CollectionUtils.isEmpty(user.getReviews())) {
            reviewRepository.deleteAll(user.getReviews());
        }
    
        if (!CollectionUtils.isEmpty(user.getRefreshTokens())) {
            tokenRepository.deleteAll(user.getRefreshTokens());
        }
    
        // Xóa User sau cùng
        userRepository.delete(user);
    }


    @Override
    public UserDTO updateUser(UserDTO user){
        User oldUser = userRepository.findUserById(user.getId());
        if(oldUser == null){
            throw new NotFoundException("Not found user for : " + user.getId());
        }
        User userMapper= UserMapper.INSTANCE.convertToUser(user);
        User newUser = userRepository.save(userMapper);
        return UserMapper.INSTANCE.convertToUserDTO(newUser);
    }
    

}
