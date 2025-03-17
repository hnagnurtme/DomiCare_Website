package com.backend.domicare.security.jwt;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.exception.EmailAlreadyExistException;
import com.backend.domicare.exception.InvalidRefreshToken;
import com.backend.domicare.exception.NotFoundException;
import com.backend.domicare.exception.UnconfirmEmailException;
import com.backend.domicare.mapper.UserMapper;
import com.backend.domicare.model.Token;
import com.backend.domicare.model.User;
import com.backend.domicare.security.dto.LoginRequest;
import com.backend.domicare.security.dto.LoginResponse;
import com.backend.domicare.security.dto.RefreshTokenRespone;
import com.backend.domicare.security.dto.RegisterResponse;
import com.backend.domicare.service.UserService;

import jakarta.transaction.Transactional;
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
        throw new UnconfirmEmailException("Email chưa được xác nhận");
    }
    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setAccessToken(accessToken);
    loginResponse.setRefreshToken(refreshToken);
    loginResponse.setUser(UserMapper.INSTANCE.convertToUserDTO(user));
    return loginResponse;
}

    public void logout() {
        SecurityContextHolder.clearContext();

    }

    public RegisterResponse register(UserDTO user) {
        String email = user.getEmail();

        if (userService.isEmailAlreadyExist(email)) {
            throw new EmailAlreadyExistException("Đã tồn tại email : " + email);
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
        registerResponse.setEmailConfirmed(userResponse.isEmailConfirmed());
        registerResponse.setRoles(userResponse.getRoles());
        return registerResponse;
    } 
    @Transactional
    public RefreshTokenRespone createAccessTokenFromRefreshToken(String refreshToken) {
        if (!jwtTokenManager.isRefreshTokenValid(refreshToken)) {
            throw new InvalidRefreshToken("Refresh token không hợp lệ");
        }

        Token token = userService.findByRefreshTokenWithUser(refreshToken);
        if (token == null) {
            throw new InvalidRefreshToken("Refresh token không hợp lệ");
        }

        User user = token.getUser(); 
        String accessToken = jwtTokenManager.createAccessToken(user.getEmail());

        return new RefreshTokenRespone(accessToken, user.getEmail());
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
