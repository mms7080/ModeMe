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
    private boolean liked; // 현재 사용자가 좋아요를 눌렀는지 여부
    
    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }
    
    public static ProductReviewDTO fromEntity(ProductReview review, ReviewLikeRepository likeRepo, User currentUser) {
    	
        ProductReviewDTO dto = new ProductReviewDTO();
        
        // 댓글 작성 시간이 있을 경우 포맷팅
        String formattedTime = "";
        if (review.getCommentedTime() != null) {
            formattedTime = review.getCommentedTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        
        // 각 필드값 할당
        dto.setId(review.getId());
        dto.setContent(review.getContent());
        dto.setCommentedTime(formattedTime);
        dto.setUsername(review.getUsers().getUsername()); // review의 작성자 정보를 가져온다고 가정
        dto.setLikeCount(likeRepo.countByReview(review));
        
        // 좋아요 여부 설정
        if (currentUser != null) {
            dto.setLiked(likeRepo.findByUserAndReview(currentUser, review).isPresent());
        } else {
            dto.setLiked(false);
        }
        
        return dto;
    }

}
