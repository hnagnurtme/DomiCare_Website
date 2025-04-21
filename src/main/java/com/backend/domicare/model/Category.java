package com.backend.domicare.model;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.backend.domicare.security.jwt.JwtTokenManager;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
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
@Table(name = "CATEGORIES")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String image;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.EAGER ,orphanRemoval = true)
    @JsonIgnore
    private List<Product> products;

    private String createBy;
    private String updateBy;
    private Instant createAt;
    private Instant updateAt;

    @PrePersist
    public void prePersist() {
        Optional<String> currentUserLogin = JwtTokenManager.getCurrentUserLogin();
        if(currentUserLogin.isPresent()) {
            this.createBy = currentUserLogin.get();
        } else {
            this.createBy = "system";
        }
        this.createAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updateAt = Instant.now();
        Optional<String> currentUserLogin = JwtTokenManager.getCurrentUserLogin();
        if(currentUserLogin.isPresent()) {
            this.updateBy = currentUserLogin.get();
        } else {
            this.updateBy = "system";
        }
    }
}
