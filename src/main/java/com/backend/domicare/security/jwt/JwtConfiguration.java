package com.backend.domicare.security.jwt;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
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
            throw new IllegalArgumentException("❌ Secret key không được để trống!");
        }

        byte[] secretBytes = Base64.from(secret).decode();
        return new SecretKeySpec(secretBytes, "HmacSHA256"); // Dùng thuật toán HMAC-SHA256 mặc định
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
            .macAlgorithm(JwtTokenManager.JWT_ALGORITHM).build();

        return token -> {
            try {
                return jwtDecoder.decode(token);
            } catch (Exception e) {
                System.out.println(">>> Token Error: " + e.getMessage());
                throw e;
            }
        };
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        // Tạo JwtGrantedAuthoritiesConverter để chuyển đổi quyền từ claim trong JWT
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        
        // Nếu muốn sử dụng tiền tố "ROLE_" cho quyền, hãy bật phần này
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");  // hoặc để rỗng nếu không dùng tiền tố
        
        // Đặt tên của claim chứa danh sách các quyền của người dùng trong token
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");  // Đảm bảo claim này có trong JWT
        
        // Tạo JwtAuthenticationConverter và thiết lập JwtGrantedAuthoritiesConverter
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        
        return jwtAuthenticationConverter;
    }
    
}
