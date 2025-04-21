package com.backend.domicare.model;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.backend.domicare.security.jwt.JwtTokenManager;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
@Table(name = "ROLES")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private boolean active;
    private String createBy;
    private String updateBy;
    private Instant createAt;
    private Instant updateAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("roles") 
    @JoinTable(
        name = "permissions_roles",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<Permission> permissions;

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

    @JsonCreator
    public static Role fromString(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        return role;
    }

    @JsonValue
    public String toValue() {
        return this.name;
    }
}
