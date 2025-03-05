package com.backend.domicare.security.jwt;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import com.backend.domicare.model.Role;
import com.backend.domicare.model.User;
import com.backend.domicare.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenManager {
    private final JwtProperties jwtProperties;
    private final JwtEncoder jwtEncoder;
    private final UserService userService;
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;

    public String createAccessToken(String email) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(jwtProperties.getExpirationMinutes() * 60);

        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Không tìm thấy người dùng");
        }
        // Role role = user.getRole();
        // if (role == null) {
        //     throw new IllegalArgumentException("Không tìm thấy quyền của người dùng");
        // }
        Set<Role> roles = user.getRoles();
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy quyền của người dùng");
        }
        Role listRole = roles.iterator().next();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(email)
                .issuedAt(now)
                .expiresAt(expiration)
                .claim("email", email) 
                .claim("role", listRole.getName())
                .build();       
        JwsHeader header = JwsHeader.with(JWT_ALGORITHM).build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext context = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(context.getAuthentication()));
    }

   private static String extractPrincipal(Authentication authentication) {
    if (authentication == null) {
        return null;
    }

    Object principal = authentication.getPrincipal();

    if (principal instanceof UserDetails) {
        return ((UserDetails) principal).getUsername(); 
    } else if (principal instanceof Jwt) {
        return ((Jwt) principal).getSubject();
    } else if (principal instanceof String) {
        return (String) principal;
    }
    return null;
}

}
