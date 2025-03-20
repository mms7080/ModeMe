package com.example.Modeme.prdDetail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Modeme.Manager.Entity.AddItem;
import com.example.Modeme.Manager.Entity.ItemColor;
import com.example.Modeme.Manager.Entity.ItemColorName;
import com.example.Modeme.Manager.Entity.ItemSize;
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
	    // 기존 상품 조회
	    AddItem existingItem = addItemRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + id));

	    // --- 기본 데이터 업데이트 ---
	    existingItem.setName(updatedItem.getName());
	    existingItem.setStock(updatedItem.getStock());
	    existingItem.setPrice(updatedItem.getPrice());
	    existingItem.setCategory(updatedItem.getCategory());
	    existingItem.setSubcategory(updatedItem.getSubcategory());
	    existingItem.setProductDescription(updatedItem.getProductDescription());

	    /*
	     * 수정된 코드에서는:
	     * 기존 엔티티의 리스트를 유지한 채, 값만 수정하거나 추가/삭제하여 ID 보존
	     */

	    // --- 색상 업데이트 ---
	    List<ItemColor> existingColors = existingItem.getColors(); // 기존 색상 리스트
	    List<String> updatedColors = updatedItem.getColors();      // 새로 전달된 색상 데이터

	    // 기존 색상 엔티티 수정
	    for (int i = 0; i < updatedColors.size(); i++) {
	        if (i < existingColors.size()) {
	            // 기존 색상 엔티티가 있으면 값만 수정
	            existingColors.get(i).setColor(updatedColors.get(i));
	        } else {
	            // 기존에 없는 색상은 새로 추가
	            ItemColor newColor = new ItemColor();
	            newColor.setColor(updatedColors.get(i));
	            newColor.setAddItem(existingItem); // 연관관계 설정
	            existingColors.add(newColor);
	        }
	    }
	    // 기존 색상 개수가 더 많으면, 불필요한 엔티티 제거
	    if (existingColors.size() > updatedColors.size()) {
	        existingColors.subList(updatedColors.size(), existingColors.size()).clear();
	    }

	    // --- 색상 이름 업데이트 (원리 동일) ---
	    List<ItemColorName> existingColorNames = existingItem.getColorNames();
	    List<String> updatedColorNames = updatedItem.getColorNames();

	    for (int i = 0; i < updatedColorNames.size(); i++) {
	        if (i < existingColorNames.size()) {
	            existingColorNames.get(i).setColorName(updatedColorNames.get(i));
	        } else {
	            ItemColorName newColorName = new ItemColorName();
	            newColorName.setColorName(updatedColorNames.get(i));
	            newColorName.setAddItem(existingItem);
	            existingColorNames.add(newColorName);
	        }
	    }
	    if (existingColorNames.size() > updatedColorNames.size()) {
	        existingColorNames.subList(updatedColorNames.size(), existingColorNames.size()).clear();
	    }

	    // --- 사이즈 업데이트 (원리 동일) ---
	    List<ItemSize> existingSizes = existingItem.getProductSizes();
	    List<String> updatedSizes = updatedItem.getProductSizes();

	    for (int i = 0; i < updatedSizes.size(); i++) {
	        if (i < existingSizes.size()) {
	            existingSizes.get(i).setItemSize(updatedSizes.get(i));
	        } else {
	            ItemSize newSize = new ItemSize();
	            newSize.setItemSize(updatedSizes.get(i));
	            newSize.setAddItem(existingItem);
	            existingSizes.add(newSize);
	        }
	    }
	    if (existingSizes.size() > updatedSizes.size()) {
	        existingSizes.subList(updatedSizes.size(), existingSizes.size()).clear();
	    }

	    /*
	     * 기존 이미지 업데이트 로직은 동일 (이미지는 삭제 후 새로 추가하는 구조)
	     * => 만약 이미지도 기존 ID 유지 원하면 비슷한 방식으로 수정 가능
	     */
	    if (updatedItem.getImageUrls() != null && !updatedItem.getImageUrls().isEmpty()) {
	        productImageRepository.deleteByAddItemId(existingItem.getId()); // 기존 이미지 삭제
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
