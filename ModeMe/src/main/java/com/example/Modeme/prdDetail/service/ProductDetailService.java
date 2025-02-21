package com.example.Modeme.prdDetail.service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.Modeme.Manager.Entity.AddItem;
import com.example.Modeme.Manager.ManagerRepository.AddItemRepository;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;
import com.example.Modeme.User.UserService.UserService;
import com.example.Modeme.prdDetail.entity.ProductReview;
import com.example.Modeme.prdDetail.entity.ReviewImage;
import com.example.Modeme.prdDetail.entity.ReviewLike;
import com.example.Modeme.prdDetail.repository.ProductReviewRepository;
import com.example.Modeme.prdDetail.repository.ReviewImageRepository;
import com.example.Modeme.prdDetail.repository.ReviewLikeRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductDetailService {
	
	@Autowired
	private final AddItemRepository addItemRepository;
	
	@Autowired
	private final ProductReviewRepository reviewRepository;
	
	@Autowired
	private final ReviewLikeRepository reviewLikeRepository;
	
	@Autowired
	private final ReviewImageRepository reviewImageRepository;
	
	@Autowired
    private final UserService userService;
	
	@Autowired
    private final UserRepository userRepository;

    public AddItem findProductById(Long id) {
        return addItemRepository.findById(id)
                .orElseThrow(() -> {
                    System.err.println("AddItem ID " + id + " not found in database");
                    return new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + id);
                });
    }
    
    @Transactional
    public ProductReview saveReview(Long addItemId, String username, String content, List<String> imageUrls) {
        // 상품 조회
        AddItem product = addItemRepository.findById(addItemId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + addItemId));
        
        // 사용자 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // 리뷰 엔티티 생성 및 필드 설정
        ProductReview review = new ProductReview();
        review.setAddItem(product);
        review.setUsers(user);
        review.setContent(content);
        review.setCommentedTime(LocalDateTime.now());
        
        // 리뷰 저장
        ProductReview savedReview = reviewRepository.save(review);
        
        // 이미지 URL이 전달되었다면 ReviewImage 엔티티 생성하여 저장
        if (imageUrls != null) {
            for (String url : imageUrls) {
                if (url != null && !url.trim().isEmpty()) {
                    ReviewImage reviewImage = new ReviewImage();
                    reviewImage.setImageUrl(url);
                    reviewImage.setReview(savedReview);
                    reviewImageRepository.save(reviewImage);
                }
            }
        }
        
        return savedReview;
    }
    
    public Page<ProductReview> getReviewsByProductId(Long addItemId, int page) {
    	Pageable pageable = PageRequest.of(page, 8); // 한 페이지에 8개씩 표시
    	return reviewRepository.findByAddItemId(addItemId, pageable);
    }
    
    // 리뷰 개수를 AddItem ID 기준으로 조회
    public int getReviewCount(Long addItemId) {
        return reviewRepository.countByAddItemId(addItemId);
    }

    // 리뷰 삭제
    public void deleteReview(Long reviewId, String username) {
    	ProductReview review = reviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다"));
    	// 권한 확인: 작성자만 삭제 가능
        if (!review.getUsers().getUsername().equals(username)) {
            throw new RuntimeException("삭제 권한이 없습니다");
        }
        
        reviewRepository.deleteById(reviewId);
    }
    
    // 리뷰 수정
    public void editReview(Long reviewId, String username, String content) throws AccessDeniedException {
    	ProductReview review = reviewRepository.findById(reviewId).orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다"));
    	
    	// 권한 확인 : 작성자만 수정 가능
    	if (!review.getUsers().getUsername().equals(username)) {
            throw new AccessDeniedException("수정 권한이 없습니다");
        }
    	
    	// 리뷰 수정
    	review.setContent(content);
    	reviewRepository.save(review);
    }
    
    @Transactional
    public boolean toggleReviewLike(Long reviewId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        ProductReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다: " + reviewId));

        Optional<ReviewLike> existingLike = reviewLikeRepository.findByUserAndReview(user, review);

        if (existingLike.isPresent()) {
        	reviewLikeRepository.delete(existingLike.get());
            return false;
        } else {
            ReviewLike newLike = new ReviewLike(user, review);
            reviewLikeRepository.save(newLike);
            return true;
        }
    }

    public long getLikeCount(Long reviewId) {
        ProductReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다: " + reviewId));

        return reviewLikeRepository.countByReview(review);
    }

}
