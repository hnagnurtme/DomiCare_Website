package com.backend.domicare.service.imp;

import org.springframework.stereotype.Service;

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.exception.AccountAlreadyDelete;
import com.backend.domicare.model.User;
import com.backend.domicare.repository.UsersRepository;
import com.backend.domicare.security.jwt.JwtTokenManager;
import com.backend.domicare.service.EmailSendingService;
import com.backend.domicare.service.UserValidationService;
import com.backend.domicare.utils.ProjectConstants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserValidationServiceImp implements UserValidationService {

    private final UsersRepository userRepository;

    private final EmailSendingService emailSendingService;

    private final JwtTokenManager jwtTokenManager;

    @Override
    public boolean isEmailAlreadyExist(String email) {
    User user = userRepository.findByEmail(email);
    
    if (user == null) {
        return false; 
    }

    if (user.isDeleted()) {
        // Email existed but the user was soft-deleted; restore the account
        String randomPassword = jwtTokenManager.generateRandomPassword();
        user.setPassword(randomPassword);
        user.setDeleted(false);
        userRepository.save(user);
        emailSendingService.sendPasswordToUser(user.getEmail(), user.getName(), randomPassword);
        throw new AccountAlreadyDelete("Account has been deleted, please check your email or contact the administrator.");
    }

    return true; 
}

}
