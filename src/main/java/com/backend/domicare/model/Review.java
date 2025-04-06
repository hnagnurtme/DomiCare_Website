package com.backend.domicare.model;

import java.time.Instant;
import java.util.Optional;

import com.backend.domicare.security.jwt.JwtTokenManager;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "REVIEWS")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer rating;
    private String comment;

    private Instant createAt;
    private Instant updateAt;
    private String createBy;
    private String updateBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @PrePersist
    public void prePersist() {
        Optional<String> currentUserLogin = JwtTokenManager.getCurrentUserLogin();
        if(currentUserLogin.isPresent()) {
            this.createBy = currentUserLogin.get();
        }
        else{
            this.createBy = "system";
        }
        this.createAt = Instant.now();
    }
    @PreUpdate
    public void preUpdate() {
        this.updateAt = Instant.now();
        Optional<String> currentUserLogin = JwtTokenManager.getCurrentUserLogin();
        if(currentUserLogin.isPresent()) {
            this.updateBy= currentUserLogin.get();
        }
        else{
            this.updateBy = "system";
        }
    }
}
