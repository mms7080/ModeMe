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
    // 리뷰 삭제
    void deleteById(Long reviewId);
    // 상품 리뷰 갯수
    int countByAddItemId(Long addItemId);
    // 페이지네이션
    Page<ProductReview> findByAddItemId(Long addItemId, Pageable pageable);
	Long countByUsers(User user);
	
	void deleteByAddItemId(Long addItemId);
    // ✅ AddItem의 name을 기준으로 리뷰 검색
    Page<ProductReview> findByAddItem_NameContaining(String keyword, Pageable pageable);

    // ✅ Users의 username을 기준으로 리뷰 검색
    Page<ProductReview> findByUsers_UsernameContaining(String keyword, Pageable pageable);


}
