package com.backend.domicare.security.jwt;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.backend.domicare.dto.response.RestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{
    private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();
    private final ObjectMapper objectMapper ;
    public JwtAuthenticationEntryPoint( ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
            this.delegate.commence(request,response , authException);

            response.setContentType("application/json;charset=UTF-8");
            
            RestResponse<Object> restResponse = new RestResponse<>();

            restResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            String errorMessage = Optional.ofNullable(authException.getCause())
                .map(Throwable::getMessage)
                .orElse(authException.getMessage());
            
            restResponse.setError(errorMessage);
            restResponse.setMessage("Invalid Token");

            objectMapper.writeValue(response.getWriter(), restResponse);
    }
    
}