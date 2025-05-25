package com.backend.domicare.model;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.backend.domicare.security.jwt.JwtTokenManager;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name = "PERMISSIONS")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String apiPath;
    private String method;
    private String module;

    private String createBy;
    private String updateBy;
    private Instant createAt;
    private Instant updateAt;

    @ManyToMany(mappedBy = "permissions")
    @JsonIgnoreProperties("permissions")
    private List<Role> roles;

    @PrePersist
    public void prePersist() {
        Optional<String> currentUserLogin = JwtTokenManager.getCurrentUserLogin();
        if(currentUserLogin.isPresent() && !currentUserLogin.get().equals("anonymousUser")) {
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
        if(currentUserLogin.isPresent() && !currentUserLogin.get().equals("anonymousUser")) {
            this.updateBy= currentUserLogin.get();
        }
        else{
            this.updateBy = "system";
        }
    }
}
