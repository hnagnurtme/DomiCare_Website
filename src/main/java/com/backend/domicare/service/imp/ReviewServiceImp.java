package com.backend.domicare.service.imp;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.backend.domicare.dto.ReviewDTO;
import com.backend.domicare.dto.paging.ResultPagingDTO;
import com.backend.domicare.dto.request.ReviewRequest;
import com.backend.domicare.exception.AlreadyReviewProduct;
import com.backend.domicare.exception.NotFoundProductException;
import com.backend.domicare.exception.NotFoundUserException;
import com.backend.domicare.mapper.ReviewMapper;
import com.backend.domicare.model.Product;
import com.backend.domicare.model.Review;
import com.backend.domicare.model.User;
import com.backend.domicare.repository.ProductsRepository;
import com.backend.domicare.repository.ReviewsRepository;
import com.backend.domicare.repository.UsersRepository;
import com.backend.domicare.security.jwt.JwtTokenManager;
import com.backend.domicare.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImp implements ReviewService {
    private final ReviewsRepository reviewsRepository;
    private final UsersRepository usersRepository;
    private final ProductsRepository productsRepository;

    @Override
    public ReviewDTO getReviewById(Long id) {

        return null;
    }

    @Override
    public ReviewDTO createReview(ReviewRequest review) {
        Long productId = review.getProductId();
        Optional<String> currentUserLogin = JwtTokenManager.getCurrentUserLogin();
        final Long userId;
        if (currentUserLogin.isPresent()) {
            User user = usersRepository.findByEmail(currentUserLogin.get());
            if (user != null) {
                userId = user.getId();
            } else {
                throw new NotFoundUserException("User not found with email: " + currentUserLogin.get());
            }
        } else {
            throw new NotFoundUserException("User not found");
        }
        // Check if the product and user exist (you may need to implement these checks)
        Product product = productsRepository.findById(productId)
                .orElseThrow(() -> new NotFoundProductException("Product not found with ID: " + productId));

        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundProductException("User not found with ID: " + userId));

        // Check if the product and user exist (you may need to implement these checks)
        boolean reviewExists = reviewsRepository.existsByProductIdAndUserId(productId, userId);
        if (reviewExists) {
            throw new AlreadyReviewProduct("Review already exists for this product and user");
        }
        // Convert DTO to entity
        Review reviewEntity = ReviewMapper.INSTANCE.convertToReview(review);
        // Set the product and user in the review entity
        reviewEntity.setProduct(product);
        reviewEntity.setUser(user);
        // Save the review entity
        List<Review> reviews = product.getReviews();
        reviews.add(reviewEntity);
        product.setReviews(reviews);
        product.setOveralRating(product.calculateRatingStar());
        // Save the product entity to update the relationship
        productsRepository.save(product);

        return ReviewMapper.INSTANCE.convertToReviewDTO(reviewsRepository.save(reviewEntity));
    }

    @Override
    public ResultPagingDTO getAllReviews(Specification<Review> spec, Pageable pageable) {
        Page<Review> reviews = reviewsRepository.findAll(spec, pageable);
        ResultPagingDTO resultPagingDTO = new ResultPagingDTO();
        ResultPagingDTO.Meta meta = new ResultPagingDTO.Meta();

        meta.setPage(reviews.getNumber()+1);
        meta.setSize(reviews.getSize());
        meta.setTotal(reviews.getTotalElements());
        meta.setTotalPages(reviews.getTotalPages());

        resultPagingDTO.setMeta(meta);
        resultPagingDTO.setData(reviews.getContent());

        return resultPagingDTO;
    }
}
