package com.backend.domicare.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import com.backend.domicare.security.service.CustomOAuth2SuccessHandler;

import lombok.RequiredArgsConstructor;


@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
  
   
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) 
            .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/login","/register","/refresh-token","/email","/verify-email","/loginSuccess").permitAll()
                            .requestMatchers("/users","/users/**").hasAnyRole("ADMIN","USER")
                            .requestMatchers("/email").hasRole("ADMIN")
                            .requestMatchers("/permissions","/permissions/**").hasAnyRole("ADMIN","USER")
                            .anyRequest().authenticated()
                            )
            .formLogin(formLogin -> formLogin.disable())
            .oauth2Login(oauth2 -> oauth2
                .successHandler(customOAuth2SuccessHandler)
                .defaultSuccessUrl("/loginSuccess", true) 
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())
        )
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }

   @Bean
	public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
