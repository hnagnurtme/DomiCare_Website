package com.backend.domicare.security.jwt;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.util.StringUtils;

import com.backend.domicare.exception.NotFoundException;
import com.backend.domicare.model.Role;
import com.backend.domicare.model.Token;
import com.backend.domicare.model.User;
import com.backend.domicare.repository.TokensRepository;
import com.backend.domicare.service.UserService;

/**
 * Manages JWT token generation, validation and extraction
 * Handles both access tokens and refresh tokens
 */
@Component
public class JwtTokenManager {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenManager.class);
    
    private final JwtProperties jwtProperties;
    private final JwtEncoder jwtEncoder;
    private final UserService userService;
    private final TokensRepository tokensRepository;
    
    public JwtTokenManager(
            JwtProperties jwtProperties,
            JwtEncoder jwtEncoder,
            @org.springframework.context.annotation.Lazy UserService userService,
            TokensRepository tokensRepository) {
        this.jwtProperties = jwtProperties;
        this.jwtEncoder = jwtEncoder;
        this.userService = userService;
        this.tokensRepository = tokensRepository;
    }
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS256;
    
    private static final long REFRESH_TOKEN_DURATION_SECONDS = 604800; // 7 days

    /**
     * Creates a JWT access token for the specified user email
     * 
     * @param email User email
     * @return JWT token string
     * @throws NotFoundException if user not found
     */
    public String createAccessToken(String email) {
        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(jwtProperties.getExpirationMinutes() * 60);

        User user = userService.findUserByEmail(email);
        if (user == null) {
            logger.error("Failed to create access token: User not found with email {}", email);
            throw new NotFoundException(email + " không tồn tại");
        }

        // Get user roles or assign default if none exist
        Set<Role> roles = user.getRoles();
        Set<String> roleNames;
        
        if (roles == null || roles.isEmpty()) {
            logger.info("User {} has no roles assigned, using default ROLE_USER", email);
            roleNames = Set.of("ROLE_USER");
        } else {
            roleNames = roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        }

        // Build JWT claims
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(email)
                .issuedAt(now)
                .expiresAt(expiration)
                .claim("email", email)
                .claim("roles", roleNames)
                .build();

        JwsHeader header = JwsHeader.with(JWT_ALGORITHM).build();
        
        logger.debug("Creating access token for user {}", email);
        return this.jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    /**
     * Gets the current user login from SecurityContext
     * 
     * @return Optional containing user email if available
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext context = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(context.getAuthentication()));
    }

    /**
     * Extracts principal (username/email) from Authentication object
     */
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

    /**
     * Creates a refresh token for the specified user and stores it in the database
     * 
     * @param email User email
     * @return Refresh token string
     * @throws NotFoundException if user not found
     */
    public String createRefreshToken(String email) {
        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        User user = userService.findUserByEmail(email);
        if (user == null) {
            logger.error("Failed to create refresh token: User not found with email {}", email);
            throw new NotFoundException(email + " không tồn tại");
        }
        
        // Delete existing refresh tokens for this user to prevent token accumulation
        List<Token> existingTokens = tokensRepository.findByUserId(user.getId());
        if (existingTokens != null && !existingTokens.isEmpty()) {
            tokensRepository.deleteAll(existingTokens);
            logger.debug("Deleted existing refresh tokens for user {}", email);
        }
        
        // Generate secure random token
        String refreshToken = UUID.randomUUID().toString();
        Instant expiration = Instant.now().plusSeconds(REFRESH_TOKEN_DURATION_SECONDS);

        Token token = Token.builder()
            .refreshToken(refreshToken)
            .expiration(expiration)
            .user(user)
            .build();

        tokensRepository.save(token);
        logger.debug("Created refresh token for user {}", email);
        
        return refreshToken;
    }

    /**
     * Deletes the refresh token
     * 
     * @param refreshToken The refresh token to delete
     */
    public void deleteRefreshToken(String refreshToken) {
        if (!StringUtils.hasText(refreshToken)) {
            logger.warn("Attempted to delete null or empty refresh token");
            return;
        }
        
        try {
            User user = getUserFromRefreshToken(refreshToken);
            userService.deleteRefreshTokenByUserId(user.getId());
            logger.debug("Deleted refresh token for user ID {}", user.getId());
        } catch (NotFoundException e) {
            logger.warn("Attempted to delete non-existent refresh token");
        }
    }

    /**
     * Gets the user associated with a refresh token
     * 
     * @param refreshToken Refresh token string
     * @return User object
     * @throws NotFoundException if token not found
     */
    public User getUserFromRefreshToken(String refreshToken) {
        if (!StringUtils.hasText(refreshToken)) {
            throw new IllegalArgumentException("Refresh token cannot be empty");
        }
        
        Token token = tokensRepository.findByRefreshToken(refreshToken);
        if (token == null) {
            logger.warn("No token found for refresh token {}", refreshToken);
            throw new NotFoundException("Token không tồn tại");
        }
        return token.getUser();
    }

    /**
     * Checks if a refresh token is valid and not expired
     * 
     * @param refreshToken Refresh token string
     * @return true if valid, false otherwise
     */
    public boolean isRefreshTokenValid(String refreshToken) {
        if (!StringUtils.hasText(refreshToken)) {
            logger.warn("Attempted to validate null or empty refresh token");
            return false;
        }
        
        Token token = tokensRepository.findByRefreshToken(refreshToken);
        if (token == null) {
            logger.warn("Token validation failed: Token not found");
            return false;
        }
        
        boolean valid = token.getExpiration().isAfter(Instant.now());
        
        if (!valid) {
            logger.warn("Refresh token has expired for user ID {}", token.getUser().getId());
            // Clean up expired token
            tokensRepository.delete(token);
        }
        
        return valid;
    }

    /**
     * Generates a secure random password
     * 
     * @return Random password string
     */
    public String generateRandomPassword() {
        return UUID.randomUUID().toString();
    }
}
