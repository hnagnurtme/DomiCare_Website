package com.backend.domicare.model;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity for storing refresh tokens
 * Each token is associated with a user and has an expiration time
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
    name = "TOKENS",
    indexes = {
        @Index(name = "idx_token_refresh_token", columnList = "refresh_token"),
        @Index(name = "idx_token_user_id", columnList = "user_id")
    }
)
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "refresh_token", nullable = false, unique = true, length = 255)
    private String refreshToken;

    @Column(name = "expiration", nullable = false)
    private Instant expiration;

    @Column(name = "created_at")
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
        
        if (this.expiration == null) {
            // Default expiration: 1 day from creation
            this.expiration = Instant.now().plusSeconds(86400);
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
   
    /**
     * Check if the token has expired
     * 
     * @return true if the token expiration time is before current time
     */
    public boolean isExpired() {
        return expiration.isBefore(Instant.now());
    }
}
