package com.example.Modeme.prdDetail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Modeme.Manager.Entity.AddItem;
import com.example.Modeme.Manager.Entity.ItemColor;
import com.example.Modeme.Manager.Entity.ItemColorName;
import com.example.Modeme.Manager.Entity.ItemSize;
import com.example.Modeme.Manager.ManagerDTO.AddItemDTO;
import com.example.Modeme.Manager.ManagerRepository.AddItemRepository;
import com.example.Modeme.prdDetail.repository.ProductDetailRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductEditService {
	
	@Autowired
	private final ProductDetailRepository detailRepository;
	
	private final AddItemRepository addItemRepository;
	
	public ProductEditService(ProductDetailRepository detailRepository, AddItemRepository addItemRepository) {
		this.detailRepository = detailRepository;
		this.addItemRepository = addItemRepository;
	}

    // ID로 상품 찾기
    public AddItem findById(Long id) {
        return addItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + id));
    }

    // 상품 업데이트
    @Transactional
    public void updateProduct(Long id, AddItemDTO updatedItem) {
        AddItem existingItem = addItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + id));

        // 기존 데이터 업데이트
        existingItem.setName(updatedItem.getName());
        existingItem.setStock(updatedItem.getStock());
        existingItem.setPrice(updatedItem.getPrice());
        existingItem.setCategory(updatedItem.getCategory());
        existingItem.setSubcategory(updatedItem.getSubcategory());
        existingItem.setProductDescription(updatedItem.getProductDescription());

        // 색상 매핑
        List<ItemColor> updatedColors = updatedItem.getColors().stream()
                .map(color -> {
                    ItemColor itemColor = new ItemColor();
                    itemColor.setColor(color);
                    itemColor.setAddItem(existingItem);
                    return itemColor;
                })
                .toList();
        existingItem.getColors().clear(); // 기존 데이터 제거
        existingItem.getColors().addAll(updatedColors);

        // 색상 이름 매핑
        List<ItemColorName> updatedColorNames = updatedItem.getColorNames().stream()
                .map(colorName -> {
                    ItemColorName itemColorName = new ItemColorName();
                    itemColorName.setColorName(colorName);
                    itemColorName.setAddItem(existingItem);
                    return itemColorName;
                })
                .toList();
        existingItem.getColorNames().clear();
        existingItem.getColorNames().addAll(updatedColorNames);

    }
 }


	
