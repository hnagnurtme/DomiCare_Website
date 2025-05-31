package com.backend.domicare.repository;

import java.time.Instant;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.Review;

@Repository
public interface ReviewsRepository  extends JpaRepository<Review, Long>,  JpaSpecificationExecutor <Review> {
    boolean existsByProductIdAndUserId(Long productId, Long userId);

    //count total reviews between startDate and endDate 
    @Query("SELECT COUNT(r) FROM Review r WHERE r.createAt BETWEEN :startDate AND :endDate")
    Long countTotalReviews(Instant startDate, Instant endDate);

}
