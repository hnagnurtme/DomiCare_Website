package com.backend.domicare.service.imp;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.dto.paging.ResultPaginDTO;
import com.backend.domicare.exception.NotFoundException;
import com.backend.domicare.dto.paging.ResultPaginDTO;
import com.backend.domicare.model.Permission;
import com.backend.domicare.model.Role;
import com.backend.domicare.model.User;
import com.backend.domicare.service.RoleService;
import com.backend.domicare.service.UserService;
import com.backend.domicare.service.UserValidationService;
import com.backend.domicare.repository.UsersRepository;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class UserServiceImp implements UserService {
    private final UsersRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserValidationService userValidationService;

    private final RoleService roleService;

    @Override
    public User saveUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setAddress(userDTO.getAddress());
        user.setPhone(userDTO.getPhone());
        user.setPassword(userDTO.getPassword());
        Set<Role> roles = new HashSet<>();
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            for (Role role : userDTO.getRoles()) {
                Role managedRole = roleService.getRoleByName(role.getName()); 
                roles.add(managedRole);
            }
        } else {
            Role defaultRole = roleService.getRoleByName("ROLE_USER");
            roles.add(defaultRole);
        }
        user.setRoles(roles);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    @Override
    public User findUserByEmail(String email)  {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("Không tìm thấy người dùng " + email);
        }
        return user;
    }

    @Override
    public ResultPaginDTO getAllUsers(Specification<User> spec,Pageable pageable) {
        Page<User> users = this.userRepository.findAll(spec, pageable);
        
        ResultPaginDTO resultPaginDTO = new ResultPaginDTO();
        ResultPaginDTO.Meta meta = new ResultPaginDTO.Meta();
        
        meta.setPage(users.getNumber());
        meta.setSize(users.getSize());
        meta.setTotal(users.getTotalElements());
        meta.setTotalPages(users.getTotalPages());

        resultPaginDTO.setMeta(meta);
        resultPaginDTO.setData(users.getContent());
        return resultPaginDTO;
    }

    @Override
    public User findUserByEmailConfirmToken(String token){
        Optional<User> user = userRepository.findByEmailConfirmationToken(token);
        if(user.isPresent()){
            return user.get();
        }
        throw new NotFoundException("Không tìm thấy người dùng" + token);
    }

    @Override
    public String createVerificationToken(String email){
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new NotFoundException("Không tìm thấy người dùng" + email);
        }
        String token = java.util.UUID.randomUUID().toString();
        user.setEmailConfirmationToken(token);
        userRepository.save(user);
        return token;
    }

    @Override
    public User updateUserInfo(User user){

        return userRepository.save(user);
    }
    @Override
    public boolean isEmailAlreadyExist(String email){
        return userValidationService.isEmailAlreadyExist(email);
    }
}
