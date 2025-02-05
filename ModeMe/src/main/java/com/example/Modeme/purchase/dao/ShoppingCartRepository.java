package com.example.Modeme.purchase.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Modeme.purchase.dto.ShoppingCart;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    List<ShoppingCart> findByUserId(Long userId);
    Optional<ShoppingCart> findByUserIdAndProductId(Long userId, Long productId);
}
