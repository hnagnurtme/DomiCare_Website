package com.backend.domicare.security.jwt;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JwtConfiguration {
    private final JwtProperties jwtProperties;
    
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    private SecretKey getSecretKey() {
        String secret = jwtProperties.getSecretKey();
        
        if (secret == null || secret.isEmpty()) {
            throw new IllegalArgumentException(" Secret key không được để trống!");
        }

        byte[] secretBytes = Base64.from(secret).decode();
        return new SecretKeySpec(secretBytes, "HmacSHA256"); 
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
            .macAlgorithm(JwtTokenManager.JWT_ALGORITHM).build();

        return token -> {
            try {
                return jwtDecoder.decode(token);
            } catch (JwtException e) {
                System.out.println(">>> Token Error: " + e.getMessage());
                throw e;
            }
        };
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");  
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");  
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
    
}
