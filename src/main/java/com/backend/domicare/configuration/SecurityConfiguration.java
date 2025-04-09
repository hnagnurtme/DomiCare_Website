package com.backend.domicare.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.backend.domicare.dto.response.Message;
import com.backend.domicare.security.utils.CustomLogoutHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final CustomLogoutHandler customLogoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .requiresChannel(channel -> {
                    channel.anyRequest().requiresSecure();
                })
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/files/**", "/swagger-ui.html")
                        .permitAll()

                        // Auth
                        .requestMatchers("/login", "/register", "/refresh-token", "/verify-email", "/oauth2/**",
                                "/forgot-password", "/reset-password")
                        .permitAll()

                        // Users
                        .requestMatchers("/users","/users/**").hasAnyRole("ADMIN","USER")
                        
                        //Email Sending
                        .requestMatchers("/email").hasRole("ADMIN")

                        .requestMatchers("/permissions", "/permissions/**").hasAnyRole("ADMIN", "USER")

                        // Product CRUD operations
                        .requestMatchers(HttpMethod.GET, "/api/products", "/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/products", "/api/products/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products", "/api/products/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products", "/api/products/**").hasAnyRole("ADMIN")

                        // Category
                        .requestMatchers(HttpMethod.GET, "/api/categories", "/api/categories/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/categories", "/api/categories/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories", "/api/categories/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories", "/api/categories/**").hasAnyRole("ADMIN")

                        // Review
                        .requestMatchers(HttpMethod.GET, "/api/reviews", "/api/reviews/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/reviews", "/api/reviews/**").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/reviews", "/api/reviews/**").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/reviews", "/api/reviews/**").hasAnyRole("USER")

                        //File
                        .requestMatchers(HttpMethod.GET, "/api/cloudinary/files", "/api/cloudinary/files/**").authenticated()

                        // Cloudinary
                        .anyRequest().permitAll())

                .formLogin(formLogin -> formLogin.disable())

                .logout(logoutCustomizer -> logoutCustomizer
                        .logoutUrl("/logout")
                        .addLogoutHandler(customLogoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK); // 200 OK
                            response.setContentType("application/json"); // Đặt kiểu nội dung là JSON
                    
                            // Tạo đối tượng Message
                            Message message = new Message("Logout successful");
                            // Chuyển đối tượng Message thành JSON và trả về
                            ObjectMapper objectMapper = new ObjectMapper();
                            String jsonResponse = objectMapper.writeValueAsString(message);
                    
                            // Gửi phản hồi JSON
                            response.getWriter().write(jsonResponse);
                            response.getWriter().flush();
                        })
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        }))
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()))
                
                //Oauth2
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                // Cấu hình session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


}
