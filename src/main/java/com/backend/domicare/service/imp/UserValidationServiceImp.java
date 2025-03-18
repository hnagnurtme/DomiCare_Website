package com.backend.domicare.service.imp;

import org.springframework.stereotype.Service;

import com.backend.domicare.repository.UsersRepository;
import com.backend.domicare.service.UserValidationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserValidationServiceImp implements UserValidationService {

    private final UsersRepository userRepository;

    @Override
    public boolean isEmailAlreadyExist(String email) {
        return userRepository.existsByEmail(email);
    }
    
}
