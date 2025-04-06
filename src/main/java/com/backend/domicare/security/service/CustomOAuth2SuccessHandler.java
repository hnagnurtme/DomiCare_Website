package com.backend.domicare.security.service;

import org.springframework.stereotype.Component;

import com.backend.domicare.mapper.UserMapper;
import com.backend.domicare.model.User;
import com.backend.domicare.repository.UsersRepository;
import com.backend.domicare.security.dto.LoginResponse;
import com.backend.domicare.security.jwt.JwtTokenManager;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler  {


    private final UsersRepository userRepository;

    private final JwtTokenManager jwtTokenManager;

    
    // Xử lý khi người dùng đăng nhập thành công qua OAuth2
    public LoginResponse handleOAuth2( String email , String name,  String picture , String locale ,String subId ) {
       // Kiểm tra xem người dùng đã tồn tại trong cơ sở dữ liệu chưa
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setAvatar(picture);
            user.setAddress(locale);
            user.setGoogleId(subId);
            userRepository.save(user);
        } else {
            // Cập nhật thông tin người dùng nếu đã tồn tại
            user.setGoogleId(subId);
            userRepository.save(user);
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

}
