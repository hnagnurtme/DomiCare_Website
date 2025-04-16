package com.backend.domicare.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.backend.domicare.dto.ReviewDTO;
import com.backend.domicare.dto.request.ReviewRequest;
import com.backend.domicare.model.Review;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface  ReviewMapper {
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);
    Review convertToReview(ReviewDTO reviewDTO);
   

    List<ReviewDTO> convertToReviewDTOs(List<Review> reviews);
    List<Review> convertToReviews(List<ReviewDTO> reviewDTOs);
    default ReviewDTO convertToReviewDTO(Review review) {
        if (review == null) {
            return null;
        }
        return ReviewDTO.builder()
                .id(review.getId())
                .comment(review.getComment())
                .createAt(review.getCreateAt())
                .createBy(review.getCreateBy())
                .updateAt(review.getUpdateAt())
                .updateBy(review.getUpdateBy())
                .productId(review.getProduct() != null ? review.getProduct().getId() : null)
                .userDTO(UserMapper.INSTANCE.convertToUserDTO(review.getUser()))
                .rating(review.getRating())
                .build();
    }

    Review convertToReview(ReviewRequest reviewRequest);
    
}
