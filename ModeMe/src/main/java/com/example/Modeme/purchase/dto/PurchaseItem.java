package com.example.Modeme.purchase.dto;

import lombok.Data;

@Data
public class PurchaseItem {
    private Long productId;
    private String productName;
    private int price;
    private int quantity;
}
