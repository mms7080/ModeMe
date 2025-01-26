package com.example.Modeme.purchase.dao;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Modeme.Mypage.MypageEntity.Address;
import com.example.Modeme.purchase.dto.Purchase;

@Repository
@Scope("singleton")
public interface PurchaseRepository extends JpaRepository<Purchase, Long>{
	List<Purchase> findByUsername(String username);
	
	
}
