package com.backend.domicare.model;

import java.time.Instant;
import java.util.Optional;

import org.hibernate.annotations.Where;

import com.backend.domicare.security.jwt.JwtTokenManager;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "FILES")
@Where(clause = "is_deleted = false")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private   Long id;

    private  String createBy;
    private  String updateBy;
    private  Instant createdAt;
    private  Instant updatedAt;
    private String url;
    private String name;
    private String type;
    private String size;
    private boolean isDeleted;
    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        Optional<String> currentUserLogin = JwtTokenManager.getCurrentUserLogin();
        if(currentUserLogin.isPresent()) {
            this.createBy = currentUserLogin.get();
        } else {
            this.createBy = "system";
        }
    }
    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
        Optional<String> currentUserLogin = JwtTokenManager.getCurrentUserLogin();
        if(currentUserLogin.isPresent()) {
            this.updateBy = currentUserLogin.get();
        } else {
            this.updateBy = "system";
        }
    }


}
