package com.example.Modeme.prdDetail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Modeme.prdDetail.entity.ProductBoard;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductBoard, Long> {
	
	
}
