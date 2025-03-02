package com.backend.domicare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.Review;

@Repository
public interface ReviewsRepository  extends JpaRepository<Review, Long> {
    
}
