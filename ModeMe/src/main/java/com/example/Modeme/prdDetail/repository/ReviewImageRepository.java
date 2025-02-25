package com.example.Modeme.prdDetail.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Modeme.prdDetail.entity.ReviewImage;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    List<ReviewImage> findByReviewId(Long reviewId);

	void deleteByReviewId(Long reviewId);

	int countByReviewId(Long id);
    
}