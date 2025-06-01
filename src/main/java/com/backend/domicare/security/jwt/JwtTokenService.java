package com.backend.domicare.security.jwt;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import com.backend.domicare.dto.UserDTO;
import com.backend.domicare.exception.EmailAlreadyExistException;
import com.backend.domicare.exception.InvalidEmailOrPassword;
import com.backend.domicare.exception.InvalidRefreshToken;
import com.backend.domicare.exception.NotFoundUserException;
import com.backend.domicare.exception.UnconfirmEmailException;
import com.backend.domicare.exception.UserNotActiveException;
import com.backend.domicare.mapper.UserMapper;
import com.backend.domicare.model.Token;
import com.backend.domicare.model.User;
import com.backend.domicare.repository.TokensRepository;
import com.backend.domicare.security.dto.LoginRequest;
import com.backend.domicare.security.dto.LoginResponse;
import com.backend.domicare.security.dto.RefreshTokenRespone;
import com.backend.domicare.security.dto.RegisterRequest;
import com.backend.domicare.security.dto.RegisterResponse;
import com.backend.domicare.service.EmailSendingService;
import com.backend.domicare.service.UserService;
import com.backend.domicare.utils.ProjectConstants;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@RequiredArgsConstructor
@Service
@Transactional
@Lazy
@Slf4j
public class JwtTokenService {

    private final TokensRepository tokensRepository;
    private final JwtTokenManager jwtTokenManager;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final EmailSendingService emailSendingService;
    private final JwtDecoder jwtDecoder;

    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtTokenManager.createAccessToken(authentication.getName());
            String refreshToken = jwtTokenManager.createRefreshToken(authentication.getName());

            User user = userService.findUserByEmail(email);
            if (user == null) {
                throw new NotFoundUserException("Không tìm thấy người dùng với email: " + email);
            }

            if (!user.isEmailConfirmed()) {
                throw new UnconfirmEmailException("Email chưa được xác nhận");
            }
            if (!user.isActive()) {
                throw new UserNotActiveException("Tài khoản đã bị khóa");
            }

            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setAccessToken(accessToken);
            loginResponse.setRefreshToken(refreshToken);
            loginResponse.setUser(UserMapper.INSTANCE.convertToUserDTO(user));
            return loginResponse;

        } catch (InternalAuthenticationServiceException e) {
            log.warn("Đăng nhập thất bại cho email {}: {}", email, e.getMessage());
            throw new InvalidEmailOrPassword("Email chưa được đăng ký hoặc mật khẩu sai");
        }
    }

    public RegisterResponse register(RegisterRequest request) {
        String email = request.getEmail();
        if (userService.isEmailAlreadyExist(email)) {
            throw new EmailAlreadyExistException("Đã tồn tại email: " + email);
        }

        UserDTO newUser = new UserDTO();
        newUser.setEmail(email);
        newUser.setPassword(request.getPassword());
        newUser.setEmailConfirmed(false);
        newUser.setAvatar(ProjectConstants.DEFAULT_AVATAR_OTHER);
        newUser.setIsActive(true);

        UserDTO savedUser = userService.saveUser(newUser);
        RegisterResponse response = UserMapper.INSTANCE.convertToRegisterResponse(savedUser);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                emailSendingService.sendEmailFromTemplate(
                        email,
                        "Xác nhận đăng ký tài khoản DomiCare",
                        "SendingOTP",
                        EmailSendingService.TemplateType.VERIFICATION.name())
                        .thenAccept(token -> log.info("Đã gửi email xác minh đến {} với token: {}", email, token))
                        .exceptionally(ex -> {
                            log.error("Lỗi khi gửi email xác minh tới {}: {}", email, ex.getMessage(), ex);
                            return null;
                        });
            }
        });

        return response;
    }

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
        if (user == null) {
            throw new NotFoundUserException("Không tìm thấy người dùng");
        }

        user.setEmailConfirmed(true);
        user.setEmailConfirmationToken(null);
        userService.updateConfirmedEmail(user);
    }

    public String verifyEmailAndGetEmail(String token) {
        UserDTO user = userService.findUserByEmailConfirmToken(token);
        if (user == null) {
            throw new NotFoundUserException("Không tìm thấy người dùng");
        }

        user.setEmailConfirmed(true);
        user.setEmailConfirmationToken(null);
        userService.updateConfirmedEmail(user);
        return user.getEmail();
    }

    public String getEmailFromAccessToken(String accessToken) {
        Jwt jwt = jwtDecoder.decode(accessToken);
        String email = jwt.getClaimAsString("email");
        if (email == null) {
            throw new NotFoundUserException("Không tìm thấy email trong token");
        }
        return email;
    }

    public void logout() {
        String email = JwtTokenManager.getCurrentUserLogin()
                .orElseThrow(() -> new NotFoundUserException("Không tìm thấy người dùng"));

        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new NotFoundUserException("Không tìm thấy người dùng");
        }

        tokensRepository.deleteByUserId(user.getId());
        SecurityContextHolder.clearContext();
        log.info("User {} logged out and tokens cleared", email);
    }
}