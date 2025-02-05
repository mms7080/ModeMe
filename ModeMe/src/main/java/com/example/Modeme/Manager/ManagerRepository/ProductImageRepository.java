package com.example.Modeme.Manager.ManagerRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.Modeme.Manager.Entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    
    @Modifying
    @Transactional
    @Query("DELETE FROM ProductImage pi WHERE pi.addItem.id = :id")
    void deleteByAddItemId(@Param("id") Long id);
}

