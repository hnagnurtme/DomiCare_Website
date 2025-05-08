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
    
    /**
     * Xử lý khi người dùng đăng nhập thành công qua OAuth2
     * 
     * @param email Email của người dùng
     * @param name Tên của người dùng
     * @param picture URL hình ảnh của người dùng
     * @param locale Địa chỉ/ngôn ngữ của người dùng
     * @param subId ID từ Google
     * @return LoginResponse chứa thông tin người dùng và JWT tokens
     * @throws IllegalArgumentException nếu email không hợp lệ
     */
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
        
        // Cập nhật thông tin người dùng
        updateUserInformation(user, email, name, picture, locale, subId);
        
        // Lưu hoặc cập nhật người dùng
        if (isNewUser) {
            this.userService.saveUser(UserMapper.INSTANCE.convertToUserDTO(user));
        } else {
            this.userRepository.save(user);
        }

        // Tạo JWT token cho người dùng
        String accessToken = jwtTokenManager.createAccessToken(email);
        String refreshToken = jwtTokenManager.createRefreshToken(email);

        // Tạo LoginResponse chứa thông tin người dùng và token
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setUser(UserMapper.INSTANCE.convertToUserDTO(user));
        
        return loginResponse;
    }
    
    /**
     * Cập nhật thông tin người dùng từ dữ liệu OAuth2
     */
    private void updateUserInformation(User user, String email, String name, String picture, String locale, String subId) {
        user.setGoogleId(subId);
        user.setEmailConfirmed(true);
        user.setEmailConfirmationToken(null);
        user.setActive(true);
        user.setDeleted(false);
        
        if (StringUtils.hasText(email)) {
            user.setEmail(email);
        }
        
        if (StringUtils.hasText(name)) {
            user.setName(name);
        }
        
        if (StringUtils.hasText(picture)) {
            user.setAvatar(picture);
        }
        
        if (StringUtils.hasText(locale)) {
            user.setAddress(locale);
        }
    }
}
