package com.example.Modeme.Manager.ManagerService;

import java.beans.Transient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Modeme.Manager.Entity.AddItem;
//import com.example.Modeme.Manager.Entity.ProductImage;
import com.example.Modeme.Manager.ManagerDTO.AddItemDTO;
import com.example.Modeme.Manager.ManagerRepository.AddItemRepository;

import jakarta.transaction.Transactional;
//import com.example.Modeme.Manager.ManagerRepository.ProductImageRepository;

@Service
public class AddItemService {
	@Autowired
	private AddItemRepository ar;
	
//	@Autowired
//	private ProductImageRepository pir;
	@Transactional
	public AddItem addItemWithImages(AddItemDTO addItemDTO) {
		AddItem addItem = new AddItem();
		addItem.setId(addItemDTO.getId());
		addItem.setName(addItemDTO.getName());
		addItem.setStock(addItemDTO.getStock());
		addItem.setPrice(addItemDTO.getPrice());
		addItem.setColors(addItemDTO.getColors());
		addItem.setColorNames(addItemDTO.getColorNames());
		addItem.setCategory(addItemDTO.getCategory());
		addItem.setSubcategory(addItemDTO.getSubcategory());
		addItem.setProductSize(addItemDTO.getProductSize());
		addItem.setProductDescription(addItemDTO.getProductDescription());
		ar.save(addItem);
		return addItem;
	}
}
		
//		for (ProductImage image : addItemDTO.getImages()) {
//            image.setAddItem(saveItem);  // Associate the image with the product
//            pir.save(image);  // Save each image
//        }
//		return saveItem;
//				
//	}

