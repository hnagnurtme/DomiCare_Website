package com.backend.domicare.security.jwt;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.exception.EmailAlreadyExistException;
import com.backend.domicare.exception.InvalidRefreshToken;
import com.backend.domicare.exception.NotFoundException;
import com.backend.domicare.model.Role;
import com.backend.domicare.model.User;
import com.backend.domicare.security.dto.LoginRequest;
import com.backend.domicare.security.dto.LoginResponse;
import com.backend.domicare.security.dto.RefreshTokenRespone;
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

    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
    Authentication authentication = authenticationManager.authenticate(authenticationToken);

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String accessToken = jwtTokenManager.createAccessToken(authentication.getName());
    String refreshToken = jwtTokenManager.createRefreshToken(authentication.getName());

    Optional<String> emailOptional = JwtTokenManager.getCurrentUserLogin();
    if (!emailOptional.isPresent()) {
        throw new NotFoundException("Không tìm thấy người dùng");
    }
    String emailUser = emailOptional.get();
    

    User user = userService.findUserByEmail(emailUser);
    if(user == null){
        throw new NotFoundException("Không tìm thấy người dùng");
    }
    if(!user.isEmailConfirmed()){
        throw new NotFoundException("Email Not Confirmed");
    }
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setAccessToken(accessToken);
    loginResponse.setRefreshToken(refreshToken);
    loginResponse.setUser(user);
    return loginResponse;
}

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    public RegisterResponse register(UserDTO user) {
        String email = user.getEmail();

        if (userService.isEmailAlreadyExist(email)) {
            throw new EmailAlreadyExistException("Email already exists: " + email);
        }
        
        UserDTO userResponse = userService.saveUser(user);

        String token = jwtTokenManager.createAccessToken(user.getEmail());
        String refreshToken = jwtTokenManager.createRefreshToken(user.getEmail());
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setId(userResponse.getId());
        registerResponse.setEmail(userResponse.getEmail());
        registerResponse.setPassword(userResponse.getPassword());
        registerResponse.setAccessToken(token);
        registerResponse.setRefreshToken(refreshToken);

        Set<String> roles = new HashSet<>();
        for (Role role : userResponse.getRoles()) {
            roles.add(role.getName());
        }
        registerResponse.setRoles(roles);
        return registerResponse;
    } 


    public RefreshTokenRespone createAccessTokenFromRefreshToken(String refreshToken) {
        if (!jwtTokenManager.isRefreshTokenValid(refreshToken)) {
            throw new InvalidRefreshToken("Refresh token không hợp lệ");
        }

        String email = jwtTokenManager.getUserFromRefreshToken(refreshToken).getEmail();
        String accessToken =  jwtTokenManager.createAccessToken(email);
        return new RefreshTokenRespone(accessToken, email);
    }

    public void verifyEmail(String token) {
        UserDTO user = userService.findUserByEmailConfirmToken(token);
        
        if( user!=null){
            user.setEmailConfirmed(true);
            user.setEmailConfirmationToken(null);
            userService.updateUserInfo(user);
        }
        else{
            throw new NotFoundException("Không tìm thấy người dùng");
        }
    }

    public String verifyEmailAndGetEmail(String token) {
        UserDTO user = userService.findUserByEmailConfirmToken(token);
        
        if( user!=null){
            user.setEmailConfirmed(true);
            user.setEmailConfirmationToken(null);
            userService.updateUserInfo(user);
        }
        else{
            throw new NotFoundException("Không tìm thấy người dùng");
        }
        return user.getEmail();
    }

}
