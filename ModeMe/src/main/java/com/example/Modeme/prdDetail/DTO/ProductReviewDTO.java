package com.example.Modeme.prdDetail.DTO;

import java.time.format.DateTimeFormatter;

import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.prdDetail.entity.ProductReview;
import com.example.Modeme.prdDetail.repository.ReviewLikeRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewDTO {
    private Long id;
    private String content;
    private String commentedTime; // "yyyy-MM-dd HH:mm" 형식의 문자열
    private String username;      // 리뷰 작성자
    private long likeCount;         // 좋아요 개수
    private boolean likedByCurrentUser; // 현재 사용자가 좋아요를 눌렀는지 여부
    
    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }
    
    // 좋아요 정보 포함 변환
    public static ProductReviewDTO fromEntity(ProductReview review, ReviewLikeRepository likeRepo, User currentUser) {
        String formattedTime = "";
        if (review.getCommentedTime() != null) {
            formattedTime = review.getCommentedTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }

        long count = likeRepo.countByReview(review);
        boolean liked = currentUser != null && likeRepo.findByUserAndReview(currentUser, review).isPresent();

        return new ProductReviewDTO(
            review.getId(),
            review.getContent(),
            formattedTime,
            review.getUsers().getUsername(),
            count, // 좋아요 개수 반영
            liked
        );
    }

}
