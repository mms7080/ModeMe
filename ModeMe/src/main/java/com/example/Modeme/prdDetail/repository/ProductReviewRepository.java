package com.example.Modeme.prdDetail.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.prdDetail.entity.ProductReview;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    List<ProductReview> findByAddItemId(Long addItemId);

	Page<ProductReview> findByUsersUsernameContaining(String keyword, Pageable pageable);

	Page<ProductReview> findByAddItemNameContaining(String keyword, Pageable pageable);
	
    Page<ProductReview> findAll(Pageable pageable);
    
    // 사용자별 리뷰 수
    Long countByUsers(User user);
}
