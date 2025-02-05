package com.example.Modeme.Manager.ManagerRepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Modeme.Manager.Entity.AddItem;

@Repository
public interface AddItemRepository extends JpaRepository<AddItem, Long>{
	
	  // 카테고리로 검색
    Page<AddItem> findByCategoryContaining(String category, Pageable pageable);

    // 상품명으로 검색
    Page<AddItem> findByNameContaining(String name, Pageable pageable);
    //상품 검색
    List<AddItem> findByNameContainingIgnoreCase(String keyword);
}
