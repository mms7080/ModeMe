package com.example.Modeme.prdDetail.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Modeme.prdDetail.entity.ProductBoard;
import com.example.Modeme.prdDetail.entity.ProductReview;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
	// 특정 상품의 리뷰 목록 조회
    List<ProductReview> findByProductBoardId(Long productBoardId);
}
