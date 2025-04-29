package com.backend.domicare.model;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.annotations.Where;

import com.backend.domicare.security.jwt.JwtTokenManager;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
@Where(clause = "is_deleted = false")
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String name;

    @Column(name = "name_unsigned")
    private String nameUnsigned;

    @Column(unique = true)
    private String email;
    private String password;
    private String phone;
    private String address;

    private boolean isDeleted;

    private boolean isEmailConfirmed;
    @Column(name = "email_confirmation_token" , unique = true)
    private String emailConfirmationToken;

    @Column(name = "google_id", unique = true)
    private String googleId;

    private String avatar;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    private Instant dateOfBirth;
    private String createBy;
    private String updateBy;
    private Instant createAt;
    private Instant updateAt;
    @Builder.Default
    @Column(nullable = false)
    private boolean isActive = true;
    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE})
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Booking> bookings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Token> refreshTokens;

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

