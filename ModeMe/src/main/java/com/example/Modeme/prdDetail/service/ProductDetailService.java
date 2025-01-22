package com.example.Modeme.prdDetail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserService.UserService;
import com.example.Modeme.prdDetail.entity.ProductBoard;
import com.example.Modeme.prdDetail.entity.ProductReview;
import com.example.Modeme.prdDetail.repository.ProductDetailRepository;
import com.example.Modeme.prdDetail.repository.ProductReviewRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductDetailService {
	
	@Autowired
	private final ProductDetailRepository detailRepository;
	
	@Autowired
	private final ProductReviewRepository reviewRepository;
	
	@Autowired
    private final UserService userService;

    public ProductDetailService(ProductDetailRepository detailRepository, ProductReviewRepository reviewRepository, UserService userService) {
        this.detailRepository = detailRepository;
        this.userService = userService;
        this.reviewRepository = reviewRepository;
    }
	
    @Transactional
    public void saveReview(Long productBoardId, ProductReview review, String username) {
        ProductBoard productBoard = detailRepository.findById(productBoardId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + productBoardId));

        User user = userService.findByUsername(username);

        review.setProductBoard(productBoard);
        review.setUsers(user);

        reviewRepository.save(review);
    }

    public List<ProductReview> getReviewsByProductId(Long productBoardId) {
        return reviewRepository.findByProductBoardId(productBoardId);
    }

    public ProductBoard findProductById(Long id) {
        return detailRepository.findById(id)
                .orElseThrow(() -> {
                    System.err.println("ProductBoard ID " + id + " not found in database");
                    return new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + id);
                });
    }

	
}
