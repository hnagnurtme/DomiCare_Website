package com.backend.domicare.security.jwt;

import java.nio.file.attribute.UserPrincipal;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.backend.domicare.security.dto.LoginRequest;
import com.backend.domicare.security.dto.LoginResponse;
import com.backend.domicare.service.UserService;

import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class JwtTokenService {
    private final JwtTokenManager jwtTokenManager;
    private final AuthenticationManager authenticationManager;
    // private final UserService userService;

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

        System.out.println("✅ Xác thực thành công!");

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        
        String token = jwtTokenManager.createAccessToken(authentication);

        return new LoginResponse(token);
    } catch (BadCredentialsException e) {
        System.out.println("❌ Sai mật khẩu hoặc email!");
    } catch (Exception e) {
        System.out.println("❌ Lỗi trong quá trình xác thực: " + e.getMessage());
    }
    return null;
}

}
