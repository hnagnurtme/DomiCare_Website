package com.backend.domicare.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.domicare.dto.ReviewDTO;
import com.backend.domicare.dto.request.ReviewRequest;
import com.backend.domicare.model.Review;
import com.backend.domicare.service.ReviewService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public ResponseEntity<ReviewDTO> createReview(@Valid @RequestBody ReviewRequest review) {
        ReviewDTO createdReview = reviewService.createReview(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
    }

    @GetMapping("/reviews")
    public ResponseEntity<?> getAllReviews(
        // @RequestParam(defaultValue = "1") int page,
        // @RequestParam(defaultValue = "20") int size,
        // @RequestParam(required = false) String sortBy,
        // @RequestParam(required = false, defaultValue = "asc") String sortDirection,
        // @Filter Specification<Review> spec, Pageable pageable ) {
    
        // Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
         @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection,
            @Filter Specification<Review> spec, Pageable pageable) {

        if (sortBy != null && !sortBy.isEmpty()) {
        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("desc") ? Sort.Order.desc(sortBy) : Sort.Order.asc(sortBy));
        pageable = PageRequest.of(page - 1, size, sort);
        } else {
            pageable = PageRequest.of(page - 1, size);
        }
            return ResponseEntity.status(HttpStatus.OK).body(this.reviewService.getAllReviews(spec, pageable));
        }
}
