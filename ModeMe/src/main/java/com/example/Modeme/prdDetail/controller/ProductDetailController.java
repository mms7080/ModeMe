package com.example.Modeme.prdDetail.controller;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Modeme.Manager.Entity.AddItem;
import com.example.Modeme.Manager.ManagerDTO.AddItemDTO;
import com.example.Modeme.Manager.ManagerRepository.AddItemRepository;
import com.example.Modeme.QnA.QnARepository.QnaRepository;
import com.example.Modeme.User.UserDTO.Headerlogin;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;
import com.example.Modeme.User.UserService.UserService;
import com.example.Modeme.prdDetail.DTO.ProductReviewDTO;
import com.example.Modeme.prdDetail.entity.ProductReview;
import com.example.Modeme.prdDetail.entity.ReviewLike;
import com.example.Modeme.prdDetail.repository.ProductReviewRepository;
import com.example.Modeme.prdDetail.repository.ReviewLikeRepository;
import com.example.Modeme.prdDetail.service.ProductDetailService;
import com.example.Modeme.prdDetail.service.ProductEditService;

@Controller
@RequestMapping("/productDetail")
public class ProductDetailController {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AddItemRepository addItemRepository;

	@Autowired
	private ProductReviewRepository reviewRepository;
	
	@Autowired
	private ReviewLikeRepository reviewLikeRepository;

	@Autowired
	private final UserService userService;

	@Autowired
	private final ProductDetailService detailService;

	@Autowired
	private final ProductEditService editService;

	@Autowired
	private Headerlogin keep;

	// 생성자
	public ProductDetailController(ProductDetailService detailService, UserService userService,
			ProductEditService editService, ProductReviewRepository reviewRepository) {
		this.editService = editService;
		this.detailService = detailService;
		this.userService = userService;
		this.reviewRepository = reviewRepository;
	}

	// 로그인 정보 유지
	@ModelAttribute
	public void addAttributes(Model model, Principal principal) {
		keep.headerlogin(model, principal);

		if (principal != null) {
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		}
	}

	// 수정 페이지 렌더링
	@GetMapping("/productEdit/{id}")
	public String editProductPage(@PathVariable Long id, Model model) {
		AddItem product = editService.findById(id); // 상품 데이터 가져오기
		model.addAttribute("product", product);
		return "/productDetail/productEdit"; // 수정 페이지 템플릿 이름
	}

	// 수정 처리
	@PostMapping("/productEdit/{id}")
	public String updateProduct(@PathVariable Long id, @ModelAttribute AddItemDTO productDTO,
			RedirectAttributes redirectAttributes, Principal principal) {

		if (principal == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
			return "redirect:/signin";
		}

		try {
			// 수정 로직 (서비스에서 처리 위임)
			editService.updateProduct(id, productDTO);
			redirectAttributes.addFlashAttribute("successMessage", "상품이 성공적으로 수정되었습니다.");
		} catch (IllegalArgumentException e) {
			System.out.println("예외 발생: " + e.getMessage());
			redirectAttributes.addFlashAttribute("errorMessage", "상품 수정 중 오류가 발생했습니다: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("알 수 없는 오류 발생: " + e.getMessage());
			redirectAttributes.addFlashAttribute("errorMessage", "알 수 없는 오류로 인해 상품 수정에 실패했습니다.");
		}

		return "redirect:/productDetail/productDetail/" + id;
	}

	// 상품 상세 정보 조회
	@GetMapping("/productDetail/{id}")
	public String getProductDetail(
	        @PathVariable Long id,
	        @RequestParam(defaultValue = "0") int page,
	        Model model,
	        Principal principal
	) {
	    // 상품 정보 조회
	    AddItem product = addItemRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + id));

	    // 리뷰 개수 조회
	    int reviewCount = detailService.getReviewCount(id);

	    // 페이지네이션 설정 (1페이지당 8개 리뷰 출력)
	    Pageable pageable = PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "commentedTime"));
	    Page<ProductReview> reviewPage = reviewRepository.findByAddItemId(id, pageable);
	    
	    User currentUser = (principal != null) 
	            ? userRepository.findByUsername(principal.getName()).orElse(null) 
	            : null;

	    // 엔티티 리스트를 DTO 리스트로 변환 (좋아요 정보 포함)
	    List<ProductReviewDTO> reviewDTOs = reviewPage.getContent().stream()
	        .map(review -> ProductReviewDTO.fromEntity(review, reviewLikeRepository, currentUser))
	        .collect(Collectors.toList());

	    // 모델에 데이터 추가
	    model.addAttribute("product", product);
	    model.addAttribute("reviewCount", reviewCount);
	    model.addAttribute("reviewPage", reviewPage);
	    model.addAttribute("reviews", reviewDTOs); // 엔티티 대신 DTO 리스트 전달
	    model.addAttribute("currentPage", reviewPage.getNumber());
	    model.addAttribute("totalPages", reviewPage.getTotalPages());
	    model.addAttribute("productDescription", product.getProductDescription());
	    // DTO에 좋아요 정보가 포함되어 있으므로 별도의 likedReviews 맵은 필요없습니다.
	    // model.addAttribute("likedReviews", likedReviews);

	    return "/productDetail/productDetail";
	}

	// 리뷰 작성 페이지
	@GetMapping("/{id}/review")
	public String reviewWritePage(@PathVariable Long id, Model model, Principal principal) {
		if (principal == null) {
			throw new IllegalArgumentException("로그인이 필요합니다.");
		}
		System.out.println("리뷰 작성 페이지 요청: ID = " + id);
		AddItem product = addItemRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + id));
		model.addAttribute("product", product);
		return "/productDetail/productReviewWrite"; // 리뷰 작성 페이지
	}

	// 리뷰 작성
	@PostMapping("/{id}/review")
	public String saveReview(@PathVariable Long id, @RequestParam String content, Principal principal) {
		if (principal == null) {
			throw new IllegalArgumentException("로그인이 필요합니다.");
		}
		String username = principal.getName();
		detailService.saveReview(id, username, content);
		
		// 정확한 상세 페이지로 리다이렉트
		return "redirect:/productDetail/productDetail/" + id;
	}
	
	
	// 리뷰 데이터 가져오기
		@GetMapping("/{id}/reviews")
		@ResponseBody // JSON 응답 강제 적용
		public ResponseEntity<Map<String, Object>> getReviews(@PathVariable Long id,
		                                                      @RequestParam(defaultValue = "0") int page,
		                                                      Principal principal) {
		    // 페이지네이션: 한 페이지에 8개씩, commentedTime 내림차순
		    Pageable pageable = PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "commentedTime"));
		    Page<ProductReview> reviewPage = reviewRepository.findByAddItemId(id, pageable);

		    final User currentUser;
		    if (principal != null) {
		        currentUser = userRepository.findByUsername(principal.getName()).orElse(null);
		    } else {
		        currentUser = null;
		    }
		    
		    // 엔티티 리스트를 DTO 리스트로 변환 (좋아요 정보 포함)
		    List<ProductReviewDTO> reviewDTOs = reviewPage.getContent().stream()
		        .map(review -> ProductReviewDTO.fromEntity(review, reviewLikeRepository, currentUser))
		        .collect(Collectors.toList());

		    Map<String, Object> response = new HashMap<>();
		    response.put("reviews", reviewDTOs);             // 현재 페이지의 DTO 리스트
		    response.put("totalPages", reviewPage.getTotalPages());
		    response.put("currentPage", reviewPage.getNumber());

		    return ResponseEntity.ok(response);
		}

	
	// 리뷰 삭제
	@PostMapping("/review/{id}/delete")
	public String deleteReview(@PathVariable Long id, Principal principal) {
	    String username = principal.getName();
	    ProductReview review = reviewRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다. ID: " + id));
	    detailService.deleteReview(id, username);
	    return "redirect:/productDetail/productDetail/" + review.getAddItem().getId();
	}
	
	// 리뷰 수정 페이지
	@GetMapping("/review/{reviewId}/edit")
	public String editReviewPage(@PathVariable Long reviewId, Model model, Principal principal) {
	    System.out.println("리뷰 수정 페이지 요청: reviewId = " + reviewId);
		String username = principal.getName();
	    ProductReview review = reviewRepository.findById(reviewId)
	            .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다. ID: " + reviewId));
	    model.addAttribute("review", review);
	    return "/productDetail/productReviewEdit"; // 리뷰 수정 페이지
	}

	// 리뷰 수정
	@PostMapping("/review/{reviewId}/edit")
	public String editReview(
	        @PathVariable Long reviewId,
	        @RequestParam String content,
	        Principal principal) throws AccessDeniedException {
		
	    if (principal == null) {
	        throw new IllegalArgumentException("로그인이 필요합니다.");
	    }
	    ProductReview review = reviewRepository.findById(reviewId).orElseThrow(() ->
	    	new IllegalArgumentException("리뷰를 찾을 수 없습니다. ID: " + reviewId));
	    String username = principal.getName();
	    detailService.editReview(reviewId, username, content);
	    // 수정 후 리다이렉트
	    return "redirect:/productDetail/productDetail/" + review.getAddItem().getId();
	}
	
	
//	@PostMapping("/review/{reviewId}/like")
//	@ResponseBody
//	public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Long reviewId, Principal principal) {
//	    if (principal == null) {
//	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "로그인이 필요합니다."));
//	    }
//
//	    String username = principal.getName();
//	    boolean isLiked = detailService.toggleReviewLike(reviewId, username);
//
//	    long likeCount = reviewLikeRepository.countByReview(reviewRepository.findById(reviewId).orElseThrow());
//
//	    // 콘솔만 확인용
//	    ProductReview review = reviewRepository.findById(1L).get();
//	    System.out.println("Like Count: " + review.getLikeCount());
//	    
//	    ProductReviewDTO dto = new ProductReviewDTO();
//	    dto.setLikeCount(review.getLikeCount());
//	    System.out.println("DTO Like Count: " + dto.getLikeCount());
//
//
//	    
//	    Map<String, Object> response = new HashMap<>();
//	    response.put("liked", isLiked);
//	    response.put("likeCount", likeCount);
//
//	    return ResponseEntity.ok(response);
//	}
	
	@PostMapping("/review/{reviewId}/like")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Long reviewId, Principal principal) {
	    System.out.println("🔥 좋아요 요청 - 리뷰 ID: " + reviewId);

	    if (principal == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "로그인이 필요합니다."));
	    }

	    User user = userRepository.findByUsername(principal.getName())
	            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

	    ProductReview review = reviewRepository.findById(reviewId)
	            .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다."));

	    Optional<ReviewLike> existingLike = reviewLikeRepository.findByUserAndReview(user, review);

	    boolean liked;
	    if (existingLike.isPresent()) {
	        reviewLikeRepository.delete(existingLike.get());
	        liked = false;
	    } else {
	        reviewLikeRepository.save(new ReviewLike(user, review));
	        liked = true;
	    }

	    long likeCount = reviewLikeRepository.countByReview(review);
	    System.out.println("좋아요 상태: " + liked + ", 총 좋아요 수: " + likeCount);

	    Map<String, Object> response = new HashMap<>();
	    response.put("likeCount", likeCount);
	    response.put("liked", liked);

	    return ResponseEntity.ok(response);
	}





}
