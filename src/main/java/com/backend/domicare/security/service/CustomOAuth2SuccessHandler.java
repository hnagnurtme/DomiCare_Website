package com.backend.domicare.security.service;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.backend.domicare.mapper.UserMapper;
import com.backend.domicare.model.User;
import com.backend.domicare.repository.UsersRepository;
import com.backend.domicare.security.dto.LoginResponse;
import com.backend.domicare.security.jwt.JwtTokenManager;
import com.backend.domicare.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler {

    private final UsersRepository userRepository;
    private final UserService userService;
    private final JwtTokenManager jwtTokenManager;
    
    public LoginResponse handleOAuth2(String email, String name, String picture, String locale, String subId) {
        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("Email is required for OAuth2 authentication");
        }
        
        User user = userRepository.findByEmail(email);
        boolean isNewUser = false;
        
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setPassword(jwtTokenManager.generateRandomPassword());
            isNewUser = true;
        }
        
        updateUserInformation(user, email, name, picture, locale, subId);
        if (isNewUser) {
            this.userService.saveUser(UserMapper.INSTANCE.convertToUserDTO(user));
        } else {
            this.userRepository.save(user);
        }

        String accessToken = jwtTokenManager.createAccessToken(email);
        String refreshToken = jwtTokenManager.createRefreshToken(email);

        User entity = userRepository.findByEmail(email);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setUser(UserMapper.INSTANCE.convertToUserDTO(entity));
        
        return loginResponse;
    }
    
    private void updateUserInformation(User user, String email, String name, String picture, String locale, String subId) {
        user.setGoogleId(subId);
        user.setEmailConfirmed(true);
        user.setEmailConfirmationToken(null);
        user.setActive(true);
        user.setDeleted(false);
        
        if (StringUtils.hasText(email) && user.getEmail() == null) {
            user.setEmail(email);
        }
        
        if (StringUtils.hasText(name) && user.getName() == null) {
            user.setName(name);
        }
        
        if (StringUtils.hasText(picture) && user.getAvatar() == null) {
            user.setAvatar(picture);
        }
        
        if (StringUtils.hasText(locale) && user.getAddress() == null) {
            user.setAddress(locale);
        }
    }
}
