package com.backend.domicare.service;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.backend.domicare.dto.ReviewDTO;
import com.backend.domicare.dto.paging.ResultPagingDTO;
import com.backend.domicare.dto.request.ReviewRequest;
import com.backend.domicare.model.Review;

public interface ReviewService {
    public ReviewDTO getReviewById(Long id);
    public ReviewDTO createReview(ReviewRequest review);
    public ResultPagingDTO getAllReviews(Specification<Review> spec , Pageable pageable);
    public Long countTotalReviews(LocalDate startDate, LocalDate endDate);
}
