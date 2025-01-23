package com.example.Modeme.prdDetail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Modeme.Manager.Entity.AddItem;

@Repository
public interface ProductDetailRepository extends JpaRepository<AddItem, Long> {
	
	
}
