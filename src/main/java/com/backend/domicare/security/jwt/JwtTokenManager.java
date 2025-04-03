package com.backend.domicare.security.jwt;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import com.backend.domicare.exception.NotFoundException;
import com.backend.domicare.model.Role;
import com.backend.domicare.model.Token;
import com.backend.domicare.model.User;
import com.backend.domicare.repository.TokensRepository;
import com.backend.domicare.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenManager {
    private final JwtProperties jwtProperties;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final UserService userService;
    private final TokensRepository tokensRepository;
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;

    public String createAccessToken(String email) {
    Instant now = Instant.now();
    Instant expiration = now.plusSeconds(jwtProperties.getExpirationMinutes() * 60);

    User user = userService.findUserByEmail(email);
    if (user == null) {
        throw new NotFoundException(email + " không tồn tại");
    }

    Set<Role> roles = user.getRoles();
    if (roles == null || roles.isEmpty()) {
        roles = Set.of(new Role(null, "ROLE_USER", email, false, email, email, expiration, expiration, null));
    }
    Set<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toSet());

    JwtClaimsSet claims = JwtClaimsSet.builder()
            .subject(email)
            .issuedAt(now)
            .expiresAt(expiration)
            .claim("email", email)
            .claim("roles", roleNames)
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

        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else if (principal instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (principal instanceof String string) {
            return string;
        }
        return null;
    }

    public String createRefreshToken(String email) {
        User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new NotFoundException(email + " không tồn tại");
        }
        String refreshToken = UUID.randomUUID().toString();
        //Set 1 week expiration time
        Instant expiration = Instant.now().plusSeconds(604800); // 7 days

        Token token = new Token();
        token.setRefreshToken(refreshToken);
        token.setExpiration(expiration);
        token.setUser(user);

        tokensRepository.save(token);

        return refreshToken;
    }

    public void deleteRefreshToken(String refreshToken) {
        User user = getUserFromRefreshToken(refreshToken);
        userService.deleteRefreshTokenByUserId(user.getId());
    }

    public User getUserFromRefreshToken(String refreshToken) {
        Token token = tokensRepository.findByRefreshToken(refreshToken);
        if( token == null) {
            throw new NotFoundException("Token không tồn tại");
        }
        return token.getUser();
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        Token token = tokensRepository.findByRefreshToken(refreshToken);
        if (token == null) {
            throw new NotFoundException("Token không tồn tại");
        }
        return token.getExpiration().isAfter(Instant.now());
    }
}
