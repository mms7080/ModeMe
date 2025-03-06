package com.example.Modeme.Manager.ManagerRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.Modeme.Manager.Entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    
	// 주어진 상품 ID에 해당하는 모든 ProductImage를 삭제하는 쿼리
    @Modifying
    @Transactional
    @Query("DELETE FROM ProductImage pi WHERE pi.addItem.id = :id")
    void deleteByAddItemId(@Param("id") Long id);
    
    // 주어진 상품 ID에 해당하는 이미지 URL들을 첫 번째 이미지부터 오름차순으로 조회
    @Query("SELECT pi.imageUrl FROM ProductImage pi WHERE pi.addItem.id = :productId ORDER BY pi.id ASC")
    List<String> findFirstImageByProductId(@Param("productId") Long productId);
    
    // 주어진 상품 ID에 해당하는 모든 ProductImage를 조회
    List<ProductImage> findByAddItemId(Long addItemId);
}
