package com.example.Modeme.prdDetail.DTO;

import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewDTO {
    private Long id;
    private String title;
    private String content;
    private String commentedTime; // 포맷팅된 문자열로 전달 (예: "yyyy-MM-dd HH:mm")
    private String username;      // 리뷰 작성자
     
    // 엔티티(ProductReview)에서 DTO로 변환하기 위한 정적 메서드 (선택사항)
    public static ProductReviewDTO fromEntity(com.example.Modeme.prdDetail.entity.ProductReview review) {
        String formattedTime = "";
        if(review.getCommentedTime() != null) {
            formattedTime = review.getCommentedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        return new ProductReviewDTO(
            review.getId(),
            review.getTitle(),
            review.getContent(),
            formattedTime,
            review.getUsers().getUsername()
        );
    }
}

