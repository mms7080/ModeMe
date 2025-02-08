package com.example.Modeme.purchase.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PurchaseItem {
    private Long productId;
    private String productName;
    private int price;
    private int quantity;
    private String imageUrl;
    private String colorId;
    private String colorName;
    private String sizeId;
    private String sizeName;
}
