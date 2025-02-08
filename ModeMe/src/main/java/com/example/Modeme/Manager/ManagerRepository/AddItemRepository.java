package com.example.Modeme.Manager.ManagerRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    
    @Query("SELECT a FROM AddItem a LEFT JOIN FETCH a.colors WHERE a.id = :id")
    Optional<AddItem> findWithColorsById(@Param("id") Long id);

    @Query("SELECT a FROM AddItem a LEFT JOIN FETCH a.colorNames WHERE a.id = :id")
    Optional<AddItem> findWithColorNamesById(@Param("id") Long id);

}
