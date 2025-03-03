package com.backend.domicare.service.imp;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.domicare.dto.UserDTO;
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
    public User saveUser(UserDTO userDTO){
        User user = new User(); 
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setAddress(userDTO.getAddress());
        user.setPhone(userDTO.getPhone());
        user.setPassword(userDTO.getPassword());
        
        Role role = userDTO.getRole();
        if(role == null){
            role = roleService.getRoleByName("ROLE_USER");
        }
        user.setRole(role);
        userValidationService.isEmailAlreadyExist(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
