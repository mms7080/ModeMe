package com.example.Modeme.purchase.dao;

import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Modeme.purchase.dto.Purchase;

@Repository
@Scope("singleton")
public interface PurchaseRepository extends JpaRepository<Purchase, Long>{
	
	
	
}
