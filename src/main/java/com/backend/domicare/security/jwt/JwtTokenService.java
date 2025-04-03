package com.backend.domicare.security.jwt;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.exception.EmailAlreadyExistException;
import com.backend.domicare.exception.InvalidEmailOrPassword;
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
import com.backend.domicare.service.EmailSendingService;
import com.backend.domicare.service.UserService;
import com.backend.domicare.utils.ProjectConstants;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class JwtTokenService {
    private final JwtTokenManager jwtTokenManager;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final EmailSendingService emailSendingService;

        public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        // Authentication authentication = authenticationManager.authenticate(authenticationToken);
        

        // SecurityContextHolder.getContext().setAuthentication(authentication);

        // String accessToken = jwtTokenManager.createAccessToken(authentication.getName());
        // String refreshToken = jwtTokenManager.createRefreshToken(authentication.getName());

        // Optional<String> emailOptional = JwtTokenManager.getCurrentUserLogin();
        // if (!emailOptional.isPresent()) {
        //     throw new NotFoundException("Không tìm thấy người dùng");
        // }
        // String emailUser = emailOptional.get();
        

        // User user = userService.findUserByEmail(emailUser);
        // if(user == null){
        //     throw new NotFoundException("Không tìm thấy người dùng");
        // }
        // if(!user.isEmailConfirmed()){
        //     throw new UnconfirmEmailException("Email chưa được xác nhận");
        // }
        // LoginResponse loginResponse = new LoginResponse();
        // loginResponse.setAccessToken(accessToken);
        // loginResponse.setRefreshToken(refreshToken);
        // loginResponse.setUser(UserMapper.INSTANCE.convertToUserDTO(user));
        // return loginResponse;


        try {
                // Tạo đối tượng Authentication từ email và mật khẩu
                UsernamePasswordAuthenticationToken authenticationToken = 
                    new UsernamePasswordAuthenticationToken(email, password);
                
                // Xác thực tài khoản và mật khẩu
                Authentication authentication = authenticationManager.authenticate(authenticationToken);
                
                // Nếu xác thực thành công, đặt Authentication vào SecurityContext
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
            
            } catch (InternalAuthenticationServiceException e) {
                throw new InvalidEmailOrPassword("Email chưa được đăng kí ");
            } 
    }

    public void logout() {
        SecurityContextHolder.clearContext();

    }

    public RegisterResponse register(UserDTO user) {
        String email = user.getEmail();

        if (userService.isEmailAlreadyExist(email)) {
            throw new EmailAlreadyExistException("Đã tồn tại email : " + email);
        }
        user.setEmailConfirmed(false);
        user.setAvatar(ProjectConstants.DEFAULT_AVATAR);
        
        UserDTO userResponse = userService.saveUser(user);
        RegisterResponse registerResponse = UserMapper.INSTANCE.convertToRegisterResponse(userResponse);


    //     @GetMapping("/email/verify")
    // public ResponseEntity<Object> sendEmail(@RequestParam(value = "email") String email) {
    //     String emailToken = this.sendingEmailService.sendEmailFromTemplateSync(email, "Verify your account", "SendingOTP");
    //     EmailConfirmTokenResponse response = new EmailConfirmTokenResponse(email,emailToken);
    //     return ResponseEntity.status(HttpStatus.OK).body(response);
    // }
        String emailToken = this.emailSendingService.sendEmailFromTemplateSync(email, "Verify your account", "SendingOTP");

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

    public void logout(String refreshToken) {
        userService.deleteRefreshToken(refreshToken);
        SecurityContextHolder.clearContext();
    }

}
