package com.backend.domicare.security.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.backend.domicare.model.User;
import com.backend.domicare.repository.UsersRepository;
import com.backend.domicare.security.jwt.JwtTokenManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {


    private final UsersRepository userRepository;

    private final JwtTokenManager jwtTokenManager;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String googleId = (String) oAuth2User.getAttribute("sub");
        String email = (String) oAuth2User.getAttribute("email");
        String name = (String) oAuth2User.getAttribute("name");

        Optional<User> user = userRepository.findByGoogleId(googleId);
        if (user.isPresent() ) {
            String accessToken = jwtTokenManager.createAccessToken(email);
            String refreshToken = jwtTokenManager.createRefreshToken(email);
        
            response.sendRedirect("/oauth2/success?accessToken=" + accessToken 
                                                + "&refreshToken=" + refreshToken);
        }
        User userByEmail = userRepository.findByEmail(email);
        if(userByEmail != null){
            userByEmail.setGoogleId(googleId);
            userRepository.save(userByEmail);
            String accessToken = jwtTokenManager.createAccessToken(email);
            String refreshToken = jwtTokenManager.createRefreshToken(email);
            
            // redirect cả hai token về trang xử lý phía client
            response.sendRedirect("/oauth2/success?accessToken=" + accessToken 
                                                + "&refreshToken=" + refreshToken);
        }
        
        else{
            
            User newUser = User.builder()
                    .email(email)
                    .name(name)
                    .googleId(googleId)
                    .build();
            userRepository.save(newUser);
            String accessToken = jwtTokenManager.createAccessToken(email);
            String refreshToken = jwtTokenManager.createRefreshToken(email);
            
            // redirect cả hai token về trang xử lý phía client
            response.sendRedirect("/oauth2/success?accessToken=" + accessToken 
                                                + "&refreshToken=" + refreshToken);


        }
    }
}
