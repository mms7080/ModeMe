package com.example.Modeme.purchase.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(
		name = "SHOPPINGCART_SEQ",
		sequenceName = "SHOPPINGCART_SEQ",
		allocationSize = 1,
		initialValue = 1)
public class ShoppingCart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SHOPPINGCART_SEQ")
    private Long id;
    
    private Long userId; // 유저 번호
    private Long productId; // 상품 번호
    private String productName; // 상품 명
    private int quantity; // 상품 갯수
}
