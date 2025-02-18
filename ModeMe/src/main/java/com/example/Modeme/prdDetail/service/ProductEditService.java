package com.example.Modeme.prdDetail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Modeme.Manager.Entity.AddItem;
import com.example.Modeme.Manager.Entity.ItemColor;
import com.example.Modeme.Manager.Entity.ItemColorName;
import com.example.Modeme.Manager.Entity.ProductImage;
import com.example.Modeme.Manager.ManagerDTO.AddItemDTO;
import com.example.Modeme.Manager.ManagerRepository.AddItemRepository;
import com.example.Modeme.Manager.ManagerRepository.ProductImageRepository;
import com.example.Modeme.prdDetail.repository.ProductDetailRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductEditService {

	@Autowired
	private final ProductDetailRepository detailRepository;

	private final AddItemRepository addItemRepository;

	@Autowired
	private ProductImageRepository productImageRepository;

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
	public AddItem updateProduct(Long id, AddItemDTO updatedItem) {
		AddItem existingItem = addItemRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + id));

		// 기본 데이터 업데이트
		existingItem.setName(updatedItem.getName());
		existingItem.setStock(updatedItem.getStock());
		existingItem.setPrice(updatedItem.getPrice());
		existingItem.setCategory(updatedItem.getCategory());
		existingItem.setSubcategory(updatedItem.getSubcategory());
		existingItem.setProductDescription(updatedItem.getProductDescription());
		System.out.println("기본 데이터 업데이트 완료");

		// 색상 업데이트
		List<ItemColor> updatedColors = updatedItem.getColors().stream().map(color -> {
			ItemColor itemColor = new ItemColor();
			itemColor.setColor(color);
			itemColor.setAddItem(existingItem);
			return itemColor;
		}).toList();
		existingItem.getColors().clear();
		existingItem.getColors().addAll(updatedColors);
		System.out.println("색상 업데이트 완료: " + updatedColors);

		// 색상 이름 업데이트
		List<ItemColorName> updatedColorNames = updatedItem.getColorNames().stream().map(colorName -> {
			ItemColorName itemColorName = new ItemColorName();
			itemColorName.setColorName(colorName);
			itemColorName.setAddItem(existingItem);
			return itemColorName;
		}).toList();
		existingItem.getColorNames().clear();
		existingItem.getColorNames().addAll(updatedColorNames);
		System.out.println("색상 이름 업데이트 완료: " + updatedColorNames);

		// 기존 이미지 URL 유지 (새로운 이미지가 없으면 기존 이미지 유지)
		if (updatedItem.getImageUrls() != null && !updatedItem.getImageUrls().isEmpty()) {
			productImageRepository.deleteByAddItemId(existingItem.getId());
			productImageRepository.flush();

			for (String imageUrl : updatedItem.getImageUrls()) {
				if (imageUrl != null && !imageUrl.trim().isEmpty()) {
					ProductImage productImage = new ProductImage();
					productImage.setImageUrl(imageUrl);
					productImage.setAddItem(existingItem);
					productImageRepository.save(productImage);
				}
			}
		} else {
			System.out.println("이미지 URL이 전달되지 않음: 기존 이미지 유지");
		}
		return existingItem;
	}

}
