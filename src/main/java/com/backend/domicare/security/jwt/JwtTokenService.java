package com.backend.domicare.security.jwt;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.model.User;
import com.backend.domicare.security.dto.LoginRequest;
import com.backend.domicare.security.dto.LoginResponse;
import com.backend.domicare.security.dto.RegisterResponse;
import com.backend.domicare.service.UserService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class JwtTokenService {
    private final JwtTokenManager jwtTokenManager;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public LoginResponse login(LoginRequest loginRequest) {
    String email = loginRequest.getEmail();
    String password = loginRequest.getPassword();

    if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
        throw new IllegalArgumentException("Email và mật khẩu không được để trống");
    }

    System.out.println("👉 Đang xác thực: " + email);

    try {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenManager.createAccessToken(authentication.getName());
        String refreshToken = jwtTokenManager.createRefreshToken(authentication.getName());

        Optional<String> emailOptional = JwtTokenManager.getCurrentUserLogin();
        if (!emailOptional.isPresent()) {
            throw new IllegalArgumentException("Không tìm thấy người dùng");
        }
        String emailUser = emailOptional.get();
        

        User user = userService.findUserByEmail(emailUser);

        if( user == null){
            throw new IllegalArgumentException("Không tìm thấy người dùng");
        }


        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setUser(user);
        return loginResponse;
        } catch (BadCredentialsException e) {
            System.out.println("❌ Sai mật khẩu hoặc email!");
        } catch (Exception e) {
            System.out.println("❌ Lỗi trong quá trình xác thực: " + e.getMessage());
        }
        return null;
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    public RegisterResponse register(UserDTO user) {
        User userResponse = userService.saveUser(user);

        String token = jwtTokenManager.createAccessToken(user.getEmail());
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setUser(userResponse);
        registerResponse.setAccessToken(token);
        return registerResponse;
    } 


    public String createAccessTokenFromRefreshToken(String refreshToken) {
        if (!jwtTokenManager.isRefreshTokenValid(refreshToken)) {
            throw new IllegalArgumentException("Refresh token không hợp lệ");
        }

        String email = jwtTokenManager.getUserFromRefreshToken(refreshToken).getEmail();
        return jwtTokenManager.createAccessToken(email);
    }

}
