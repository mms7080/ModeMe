package com.example.Modeme.Manager.ManagerService;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.Modeme.Manager.Entity.AddItem;
import com.example.Modeme.Manager.Entity.ItemColor;
import com.example.Modeme.Manager.Entity.ItemColorName;
import com.example.Modeme.Manager.Entity.ItemSize;
import com.example.Modeme.Manager.Entity.ProductImage;
//import com.example.Modeme.Manager.Entity.ProductImage;
import com.example.Modeme.Manager.ManagerDTO.AddItemDTO;

import com.example.Modeme.Manager.ManagerRepository.AddItemRepository;
import com.example.Modeme.Manager.ManagerRepository.ProductImageRepository;
import com.example.Modeme.Manager.ManagerRepository.itemColorNameRepository;
import com.example.Modeme.Manager.ManagerRepository.itemColorRepository;
import com.example.Modeme.Manager.ManagerRepository.itemSizeRepository;
import com.example.Modeme.prdDetail.repository.ProductReviewRepository;

import jakarta.transaction.Transactional;


//import com.example.Modeme.Manager.ManagerRepository.ProductImageRepository;

@Service
public class AddItemService {
	@Autowired
	private ProductReviewRepository prr;
	
	@Autowired
	private AddItemRepository ar;	
	
	@Autowired
	private itemColorRepository icr;
	
	@Autowired
	private itemColorNameRepository inr;
	
	@Autowired
	private itemSizeRepository isr;
	
	@Autowired
    private ProductImageRepository productImageRepository; 
	
//	@Autowired
//	private ProductImageRepository pir;
	@Transactional
	public AddItem addItemWithImages(AddItemDTO addItemDTO) {
	    // AddItem 엔티티 생성 및 기본 필드 설정
	    AddItem addItem = new AddItem();
	    addItem.setName(addItemDTO.getName());
	    addItem.setStock(addItemDTO.getStock());
	    addItem.setPrice(addItemDTO.getPrice());
	    addItem.setCategory(addItemDTO.getCategory());
	    addItem.setSubcategory(addItemDTO.getSubcategory());
	    addItem.setProductDescription(addItemDTO.getProductDescription());

	    // AddItem 저장
	    AddItem savedItem = ar.save(addItem);

	    // 컬러 처리
	    if (addItemDTO.getColors() != null) {
	        for (String color : addItemDTO.getColors()) { // Handle as a List<String>
	            ItemColor itemColor = new ItemColor();
	            itemColor.setAddItem(savedItem); // Set relationship
	            itemColor.setColor(color.trim());
	            icr.save(itemColor);
	        }
	    }

	    // 컬러 이름 처리
	    if (addItemDTO.getColorNames() != null) {
	        for (String colorName : addItemDTO.getColorNames()) { // Handle as a List<String>
	            ItemColorName itemColorName = new ItemColorName();
	            itemColorName.setAddItem(savedItem); // Set relationship
	            itemColorName.setColorName(colorName.trim());
	            inr.save(itemColorName);
	        }
	    }

	    // 사이즈 처리
	    if (addItemDTO.getProductSizes() != null) {
	        for (String size : addItemDTO.getProductSizes()) { // Handle as a List<String>
	            ItemSize itemSize = new ItemSize();
	            itemSize.setAddItem(savedItem); // Set relationship
	            itemSize.setItemSize(size.trim());
	            isr.save(itemSize);
	        }
	    }
	    // 이미지 URL 저장
	    if (addItemDTO.getImageUrls() != null) {
	        for (String imageUrl : addItemDTO.getImageUrls()) {
	            ProductImage productImage = new ProductImage();
	            productImage.setImageUrl(imageUrl);
	            productImage.setAddItem(savedItem);
	            productImageRepository.save(productImage);
	        }
	    }
	    
	    if (addItemDTO.getImageUrls() != null) {
	        for (String imageUrl : addItemDTO.getImageUrls()) {
	            if (imageUrl != null && !imageUrl.trim().isEmpty()) { // ✅ NULL 및 빈 문자열 방지
	                ProductImage productImage = new ProductImage();
	                productImage.setImageUrl(imageUrl);
	                productImage.setAddItem(savedItem);
	                productImageRepository.save(productImage);
	            }
	        }
	    }
	    
	    if (addItemDTO.getImageUrls() != null && !addItemDTO.getImageUrls().isEmpty()) {
	        savedItem.setImageUrls(addItemDTO.getImageUrls());
	    } else {
	        savedItem.setImageUrls(new ArrayList<>()); // 기본값
	    }

	    return savedItem; // Return the saved AddItem instance
	}
	
	  // 기본 상품 목록 조회 (내림차순 정렬)
    public Page<AddItem> getProducts(Pageable pageable) {
        // 내림차순 정렬: id 기준으로 내림차순 정렬
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("id")));
        return ar.findAll(pageable);
    }

    // 검색된 상품 목록 조회
    public Page<AddItem> searchProducts(String option, String keyword, Pageable pageable) {
    	pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("id")));
        if ("category".equals(option) && keyword != null) {
            return ar.findByCategoryContaining(keyword,pageable);
        } else if ("name".equals(option) && keyword != null) {
            return ar.findByNameContaining(keyword, pageable);
        } else {
            return ar.findAll(pageable);
        }
    }
    
    // 상품 삭제 메서드
    @Transactional
    public void deleteProduct(Long id) {
        // 상품 삭제 처리
    	prr.deleteByAddItemId(id);
    	inr.deleteById(id);
    	icr.deleteById(id);
    	isr.deleteById(id);
        ar.deleteById(id);
        // 4️⃣ 이미지 정보 삭제
        productImageRepository.deleteByAddItemId(id);

        
    }
    
 // 상품 검색 메서드 추가
    public List<AddItem> searchItems(String keyword) {
        return ar.findByNameContainingIgnoreCase(keyword);
    }
    
    public String getFirstImageUrl(Long productId) {
        // AddItem 엔티티를 가져온 후 첫 번째 이미지 URL 반환
        AddItem addItem = ar.findById(productId).orElse(null);
        if (addItem != null && !addItem.getImageUrls().isEmpty()) {
            return addItem.getImageUrls().get(0);  // 첫 번째 이미지 URL 반환
        }
        return null;  // 이미지가 없으면 null 반환
    }
}

		

