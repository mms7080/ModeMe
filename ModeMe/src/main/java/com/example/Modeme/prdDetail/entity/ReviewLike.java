package com.example.Modeme.prdDetail.entity;

import com.example.Modeme.User.UserEntity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "REVIEW_LIKE", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"product_review_id", "user_id"})
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewLike {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ReviewLikeSeq")
    @SequenceGenerator(name = "ReviewLikeSeq", sequenceName = "ReviewLikeSeq", allocationSize = 1, initialValue = 1)
    private Long id;
    
    // 좋아요를 누른 리뷰
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_review_id", nullable = false)
    private ProductReview productReview;
    
    // 좋아요를 누른 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
