package com.example.Modeme.purchase.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Modeme.purchase.dto.Purchase;

@Repository
@Scope("singleton")
public interface PurchaseRepository extends JpaRepository<Purchase, Long>{
	List<Purchase> findByUsername(String username);
	
	int countByUsername(String username);
	
    Page<Purchase> findByProcess(String process, Pageable pageable);  // 주문 상태로 검색
    Page<Purchase> findByItemnameContaining(String itemname, Pageable pageable);  // 상품명만으로 검색
    
    Page<Purchase> findByUsernameContaining(String username, Pageable pageable);
    
	List<Purchase> findByMerchantUid(String merchantUid);

	List<Purchase> findByUserIdOrderByIdDesc(Long userId); // 유저번호로 가장 최신 결제항목 가져오기
	
	List<Purchase> findByUsernameAndProcess(String username, String process);
	
	Page<Purchase> findByUsername(String username, Pageable pageable);

	Page<Purchase> findByUsernameAndProcessAndOrderDateBetween(String username, String process, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
	
	Page<Purchase> findByUsernameAndOrderDateBetween(String username,  LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

}
