package com.backend.domicare.service.imp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.dto.paging.ResultPaginDTO;
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

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
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
}
