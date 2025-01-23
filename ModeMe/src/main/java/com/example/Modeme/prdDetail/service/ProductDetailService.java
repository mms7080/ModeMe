package com.example.Modeme.prdDetail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserService.UserService;
import com.example.Modeme.Manager.Entity.AddItem;
import com.example.Modeme.Manager.ManagerRepository.AddItemRepository;
import com.example.Modeme.prdDetail.entity.ProductReview;
import com.example.Modeme.prdDetail.repository.ProductReviewRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductDetailService {
	
	@Autowired
	private final AddItemRepository addItemRepository;
	
	@Autowired
	private final ProductReviewRepository reviewRepository;
	
	@Autowired
    private final UserService userService;

    public ProductDetailService(AddItemRepository addItemRepository, ProductReviewRepository reviewRepository, UserService userService) {
        this.addItemRepository = addItemRepository;
        this.userService = userService;
        this.reviewRepository = reviewRepository;
    }
	
    @Transactional
    public void saveReview(Long addItemId, ProductReview review, String username) {
        AddItem addItem = addItemRepository.findById(addItemId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + addItemId));

        User user = userService.findByUsername(username);

        review.setAddItem(addItem);
        review.setUsers(user);

        reviewRepository.save(review);
    }

    public List<ProductReview> getReviewsByProductId(Long addItemId) {
        return reviewRepository.findByAddItemId(addItemId);
    }

    public AddItem findProductById(Long id) {
        return addItemRepository.findById(id)
                .orElseThrow(() -> {
                    System.err.println("AddItem ID " + id + " not found in database");
                    return new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + id);
                });
    }

	
}
