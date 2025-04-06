package com.backend.domicare.model;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.backend.domicare.security.jwt.JwtTokenManager;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "PRODUCTS")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Review> reviews;
    
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

    public Float getRatingStar() {
        if (reviews == null || reviews.isEmpty()) {
            return 0f;
        }
        float totalRating = 0;
        for (Review review : reviews) {
            totalRating += review.getRating();
        }
        return totalRating / reviews.size();
    }

}
