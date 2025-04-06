package com.backend.domicare.service;

import java.util.List;

import com.backend.domicare.dto.ReviewDTO;
import com.backend.domicare.dto.request.ReviewRequest;

public interface ReviewService {
    public ReviewDTO getReviewById(Long id);
    public ReviewDTO createReview(ReviewRequest review);
    public List<ReviewDTO> getAllReviews();
}
