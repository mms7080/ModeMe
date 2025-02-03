package com.example.Modeme.prdDetail.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Modeme.prdDetail.entity.ReviewLike;
import com.example.Modeme.prdDetail.entity.ProductReview;
import com.example.Modeme.User.UserEntity.User;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
	
    // 특정 리뷰의 좋아요 개수 조회
    int countByReview(ProductReview review);
    
    // 특정 리뷰와 사용자에 해당하는 좋아요 엔티티 조회
	Optional<ReviewLike> findByUserAndReview(User user, ProductReview review);

	int countByReviewId(Long reviewId);
}
