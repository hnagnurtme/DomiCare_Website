package com.backend.domicare.configuration;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.backend.domicare.dto.response.Message;
import com.backend.domicare.security.jwt.JwtAuthenticationEntryPoint;
import com.backend.domicare.security.utils.CustomLogoutHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

/**
 * Configuration class for application security settings
 */
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity // Enables @PreAuthorize, @PostAuthorize annotations
public class SecurityConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

    // Dependencies
    private final CustomLogoutHandler customLogoutHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final ObjectMapper objectMapper;

    /**
     * API endpoint path constants
     */
    private static class ApiPaths {
        // Documentation paths
        public static final String[] DOCS_PATHS = {"/v3/api-docs/**", "/swagger-ui/**"};
        
        // Authentication paths
        public static final String[] AUTH_PATHS = {"/login", "/register", "/refresh-token",
                                                  "/verify-email", "/oauth2", "/logout",
                                                  "/forgot-password", "/reset-password"};
        
        // Public API paths
        public static final String[] PUBLIC_API_PATHS = {"/files", "/api/public/**",};
        
        // User and admin API paths
        public static final String[] USER_PATHS = {"/users/**", "/permissions/**"};
        public static final String[] ADMIN_PATHS = {"/email/**"};
        
        // Product management paths
        public static final String[] PRODUCT_ADMIN_PATHS = {"/api/products/**"};
        public static final String[] CATEGORY_ADMIN_PATHS = {"/api/categories/**"};
        public static final String[] REVIEW_USER_PATHS = {"/api/reviews/**"};
        public static final String[] FILE_PATHS = {"/api/cloudinary/files/**"};
    }

    /**
     * Role constants
     */
    private static class Roles {
        public static final String ADMIN = "ADMIN";
        public static final String USER = "USER";
    }

    /**
     * Get all public API paths
     * @return List of public endpoint paths
     */
    private List<String> getPublicApiPaths() {
        return Arrays.stream(new String[][] {
            ApiPaths.DOCS_PATHS,
            ApiPaths.AUTH_PATHS,
            ApiPaths.PUBLIC_API_PATHS
        }).flatMap(Arrays::stream).toList();
    }

    /**
     * Configuration for public endpoints that don't require authentication
     */
    @Bean
    @Order(1) // Higher priority
    public SecurityFilterChain publicEndpointFilterChain(HttpSecurity http) throws Exception {
        logger.debug("Configuring public endpoint security chain");
        http
            .securityMatcher(getPublicApiPaths().toArray(String[]::new))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf(AbstractHttpConfigurer::disable)
            .oauth2ResourceServer(oauth2 -> oauth2.disable());

        
        return http.build();
    }

    /**
     * Main security filter chain for protected endpoints
     */
    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.debug("Configuring main security filter chain");
        
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // User and admin access
                .requestMatchers(ApiPaths.USER_PATHS).permitAll()
                
                // Admin-only access
                .requestMatchers(ApiPaths.ADMIN_PATHS).hasRole(Roles.ADMIN)
                
                // Product management - admin only
                .requestMatchers(HttpMethod.POST, ApiPaths.PRODUCT_ADMIN_PATHS).hasRole(Roles.ADMIN)
                .requestMatchers(HttpMethod.PUT, ApiPaths.PRODUCT_ADMIN_PATHS).hasRole(Roles.ADMIN)
                .requestMatchers(HttpMethod.DELETE, ApiPaths.PRODUCT_ADMIN_PATHS).hasRole(Roles.ADMIN)
                
                // Category management - admin only
                .requestMatchers(HttpMethod.POST, ApiPaths.CATEGORY_ADMIN_PATHS).hasRole(Roles.ADMIN)
                .requestMatchers(HttpMethod.PUT, ApiPaths.CATEGORY_ADMIN_PATHS).hasRole(Roles.ADMIN)
                .requestMatchers(HttpMethod.DELETE, ApiPaths.CATEGORY_ADMIN_PATHS).hasRole(Roles.ADMIN)
                
                // Review management - users only
                .requestMatchers(HttpMethod.POST, ApiPaths.REVIEW_USER_PATHS).hasRole(Roles.USER)
                .requestMatchers(HttpMethod.PUT, ApiPaths.REVIEW_USER_PATHS).hasRole(Roles.USER)
                .requestMatchers(HttpMethod.DELETE, ApiPaths.REVIEW_USER_PATHS).hasRole(Roles.USER)
                
                // File access - any authenticated user
                .requestMatchers(ApiPaths.FILE_PATHS).authenticated()
                
                // Booking status - public access
                .requestMatchers(HttpMethod.GET, "/api/booking-status/**").permitAll()
                
                // Default fallback - allow access if not specified above
                .anyRequest().permitAll()
            )
            // Disable form login
            .formLogin(AbstractHttpConfigurer::disable)
            
            // Configure JWT authentication
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults())
            )
            
            
            // Configure logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .addLogoutHandler(customLogoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> {
                    logger.info("User logged out successfully: {}", 
                        authentication != null ? authentication.getName() : "anonymous");
                    
                    response.setStatus(HttpStatus.OK.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    
                    Message message = new Message("Logout successful");
                    String jsonResponse = objectMapper.writeValueAsString(message);
                    
                    response.getWriter().write(jsonResponse);
                    response.getWriter().flush();
                })
            )
            
            // Configure exception handling
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    logger.warn("Unauthorized access attempt: {}", authException.getMessage());
                    
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    
                    Message errorMessage = new Message("Unauthorized access: " + authException.getMessage());
                    String jsonResponse = objectMapper.writeValueAsString(errorMessage);
                    
                    response.getWriter().write(jsonResponse);
                    response.getWriter().flush();
                })
            )
            
            // Session management - stateless for REST API
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // Enable CORS
            .cors(Customizer.withDefaults());

        return http.build();
    }
}

