package com.backend.domicare.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.backend.domicare.security.utils.CustomLogoutHandler;

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
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/files/**", "/swagger-ui.html")
                        .permitAll()

                        .requestMatchers("/login", "/register", "/refresh-token", "/verify-email", "/oauth2/**",
                                "/forgot-password", "/reset-password")
                        .permitAll()

                        // .requestMatchers("/users","/users/**")
                        // .hasAnyRole("ADMIN","USER")

                        .requestMatchers("/email")
                        .hasRole("ADMIN")

                        .requestMatchers("/permissions", "/permissions/**")
                        .hasAnyRole("ADMIN", "USER")

                        // Product CRUD operations
                        .requestMatchers(HttpMethod.GET, "api/products", "/api/products/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/products", "/api/products/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products", "/api/products/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products", "/api/products/**").hasAnyRole("ADMIN")

                        // Category
                        .requestMatchers(HttpMethod.GET, "api/categories", "/api/categories/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/categories", "/api/categories/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories", "/api/categories/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories", "/api/categories/**").hasAnyRole("ADMIN")

                        // Cloudinary

                        .anyRequest().permitAll())
                .formLogin(formLogin -> formLogin.disable())

                .logout(logoutCustomizer -> logoutCustomizer
                        .logoutUrl("/logout")
                        .addLogoutHandler(customLogoutHandler)
                        .logoutSuccessUrl("/users")

                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/google")
                        .defaultSuccessUrl("/login/oauth2/success")
                        .failureUrl("/login/oauth2/failure"))
                        
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        }))
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()))

                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }


}
