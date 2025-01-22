package com.example.Modeme.prdDetail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Modeme.Manager.Entity.AddItem;
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

    // 상품 수정
    @Transactional
    public void updateItem(Long id, AddItem updatedItem) {
    	System.out.println("테스트");
        AddItem existingItem = addItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + id));

        // 필드 업데이트
        existingItem.setName(updatedItem.getName());
        existingItem.setStock(updatedItem.getStock());
        existingItem.setPrice(updatedItem.getPrice());
        existingItem.setColors(updatedItem.getColors());
        existingItem.setColorNames(updatedItem.getColorNames());
        existingItem.setCategory(updatedItem.getCategory());
        existingItem.setSubcategory(updatedItem.getSubcategory());
        existingItem.setProductSizes(updatedItem.getProductSizes());
        existingItem.setProductDescription(updatedItem.getProductDescription());
        System.out.println("테스트");
        // 명시적으로 저장
        addItemRepository.save(existingItem);
    }
	
}