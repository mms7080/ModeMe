package com.example.Modeme.purchase.dto;

import java.util.List;

import lombok.Data;

@Data
public class PurchaseRequest {
    private List<PurchaseItem> items;
}


