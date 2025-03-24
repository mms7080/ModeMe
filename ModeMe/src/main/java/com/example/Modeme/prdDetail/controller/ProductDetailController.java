package com.example.Modeme.prdDetail.controller;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import com.example.Modeme.Manager.Entity.ProductImage;
import com.example.Modeme.Manager.ManagerDTO.AddItemDTO;
import com.example.Modeme.Manager.ManagerRepository.AddItemRepository;
import com.example.Modeme.Manager.ManagerRepository.ProductImageRepository;
import com.example.Modeme.User.UserDTO.Headerlogin;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;
import com.example.Modeme.User.UserService.UserService;
import com.example.Modeme.prdDetail.DTO.ProductReviewDTO;
import com.example.Modeme.prdDetail.entity.ProductReview;
import com.example.Modeme.prdDetail.entity.ReviewImage;
import com.example.Modeme.prdDetail.entity.ReviewLike;
import com.example.Modeme.prdDetail.repository.ProductReviewRepository;
import com.example.Modeme.prdDetail.repository.ReviewImageRepository;
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
	private ProductImageRepository productImageRepository;

	@Autowired
	private ReviewImageRepository reviewImageRepository;

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
		// 최신 이미지 리스트 가져오기
		List<String> imageUrls = productImageRepository.findByAddItemId(product.getId()).stream()
				.map(ProductImage::getImageUrl).collect(Collectors.toList());
		model.addAttribute("product", product);
		model.addAttribute("imageUrls", imageUrls); // 최신 이미지 URL 추가
		return "productDetail/productEdit"; // 수정 페이지 템플릿 이름
	}

	// 수정 처리
	@PostMapping("/productEdit/{id}")
	public String updateProduct(@PathVariable Long id, @ModelAttribute AddItemDTO productDTO,
			@RequestParam(value = "imageUrls", required = false) List<String> imageUrls,
			RedirectAttributes redirectAttributes, Principal principal) {

		if (principal == null) {
			redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다.");
			return "redirect:/signin";
		}

		// 이미지 URL 파라미터가 전달되지 않았을 경우 빈 리스트로 초기화
		if (imageUrls == null) {
			imageUrls = new ArrayList<>();
		}
		productDTO.setImageUrls(imageUrls);

		try {
			// 수정 로직 (서비스에서 처리 위임)
			editService.updateProduct(id, productDTO);
			redirectAttributes.addFlashAttribute("successMessage", "상품이 성공적으로 수정되었습니다.");
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("errorMessage", "상품 수정 중 오류가 발생했습니다: " + e.getMessage());
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", "알 수 없는 오류로 인해 상품 수정에 실패했습니다.");
		}

		return "redirect:/productDetail/productDetail/" + id;
	}

	// 상품 상세 정보 조회
	@GetMapping("/productDetail/{id}")
	public String getProductDetail(@PathVariable Long id, @RequestParam(defaultValue = "0") int page, Model model,
			Principal principal) {
		// 상품 정보 조회
		AddItem product = addItemRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + id));

		// 리뷰 개수 조회
		int reviewCount = detailService.getReviewCount(id);

		// 페이지네이션 설정 (1페이지당 8개 리뷰 출력)
		Pageable pageable = PageRequest.of(page, 8, Sort.by(Sort.Direction.DESC, "commentedTime"));
		Page<ProductReview> reviewPage = reviewRepository.findByAddItemId(id, pageable);

		User currentUser = (principal != null) ? userRepository.findByUsername(principal.getName()).orElse(null) : null;

		// 엔티티 리스트를 DTO 리스트로 변환 (좋아요 정보 포함)
		List<ProductReviewDTO> reviewDTOs = reviewPage.getContent().stream()
				.map(review -> ProductReviewDTO.fromEntity(review, reviewLikeRepository, currentUser))
				.collect(Collectors.toList());

		// ProductImage 테이블에서 최신 이미지 URL 목록 조회
		List<String> imageUrls = productImageRepository.findByAddItemId(product.getId()).stream()
				.map(ProductImage::getImageUrl).distinct() // 중복 제거 추가
				.collect(Collectors.toList());
		model.addAttribute("imageUrls", imageUrls);

		// 모델에 데이터 추가
		model.addAttribute("product", product);
		model.addAttribute("reviewCount", reviewCount);
		model.addAttribute("reviewPage", reviewPage);
		model.addAttribute("reviews", reviewDTOs); // 엔티티 대신 DTO 리스트 전달
		model.addAttribute("currentPage", reviewPage.getNumber());
		model.addAttribute("totalPages", reviewPage.getTotalPages());
		model.addAttribute("productDescription", product.getProductDescription());

		return "productDetail/productDetail";
	}

	// 리뷰 작성 페이지
	@GetMapping("/{id}/review")
	public String reviewWritePage(@PathVariable Long id, Model model, Principal principal) {
		if (principal == null) {
			throw new IllegalArgumentException("로그인이 필요합니다.");
		}
		AddItem product = addItemRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + id));

		// 최신 이미지 리스트 가져오기
		List<String> imageUrls = productImageRepository.findByAddItemId(product.getId()).stream()
				.map(ProductImage::getImageUrl).collect(Collectors.toList());
		model.addAttribute("imageUrls", imageUrls); // 최신 이미지 리스트 추가
		model.addAttribute("product", product);
		return "productDetail/productReviewWrite"; // 리뷰 작성 페이지
	}

	@PostMapping("/{id}/review")
	public String saveReview(@PathVariable Long id, @RequestParam String content,
			@RequestParam(value = "imageUrls", required = false) List<String> imageUrls, Principal principal) {
		if (principal == null) {
			throw new IllegalArgumentException("로그인이 필요합니다.");
		}
		String username = principal.getName();
		detailService.saveReview(id, username, content, imageUrls);

		// 리뷰 작성 후 상품 상세 페이지로 리다이렉트
		return "redirect:/productDetail/productDetail/" + id;
	}

	// 리뷰 데이터 가져오기
	@GetMapping("/{id}/reviews")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getReviews(@PathVariable Long id,
			@RequestParam(defaultValue = "newest") String sortType, @RequestParam(defaultValue = "0") int page,
			Principal principal) {

		int pageSize = 8;
		Page<ProductReview> reviewPage;

		if ("mostLiked".equals(sortType)) {
			reviewPage = reviewRepository.findByAddItemIdOrderByLikes(id, PageRequest.of(page, pageSize));
		} else {
			Sort sort = "oldest".equals(sortType) ? Sort.by(Sort.Direction.ASC, "commentedTime")
					: Sort.by(Sort.Direction.DESC, "commentedTime");
			Pageable pageable = PageRequest.of(page, pageSize, sort);
			reviewPage = reviewRepository.findByAddItemId(id, pageable);
		}

		final User currentUser = (principal != null) ? userRepository.findByUsername(principal.getName()).orElse(null)
				: null;

		List<ProductReviewDTO> reviewDTOs = reviewPage.getContent().stream().map(review -> {
			ProductReviewDTO dto = ProductReviewDTO.fromEntity(review, reviewLikeRepository, currentUser);
			// 이미지가 있으면 hasImages=true. (ReviewImageRepository의 메서드 사용)
			int imageCount = reviewImageRepository.findByReviewId(review.getId()).size();
			dto.setHasImages(imageCount > 0);
			return dto;
		}).collect(Collectors.toList());

		Map<String, Object> response = new HashMap<>();
		response.put("reviews", reviewDTOs);
		response.put("totalPages", reviewPage.getTotalPages());
		response.put("currentPage", reviewPage.getNumber());

		return ResponseEntity.ok(response);
	}

	// 리뷰 상세 페이지(팝업창)
	@GetMapping("/reviewDetails/{reviewId}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getReviewDetails(@PathVariable Long reviewId) {
		// 리뷰 조회 (없으면 예외 발생)
		ProductReview review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다. ID: " + reviewId));

		// 리뷰 이미지 조회 (ReviewImageRepository가 주입되어 있어야 합니다)
		List<ReviewImage> reviewImages = reviewImageRepository.findByReviewId(reviewId);
		List<String> imageUrls = reviewImages.stream().map(ReviewImage::getImageUrl).collect(Collectors.toList());

		Map<String, Object> data = new HashMap<>();
		data.put("content", review.getContent());
		data.put("username", review.getUsers().getUsername());
		data.put("commentedTime",
				review.getCommentedTime() != null
						? review.getCommentedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
						: "");
		data.put("imageUrls", imageUrls);

		return ResponseEntity.ok(data);
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
		String username = principal.getName();
		ProductReview review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다. ID: " + reviewId));
		// 기존 리뷰 이미지 목록
		List<ReviewImage> reviewImages = reviewImageRepository.findByReviewId(reviewId);
		// **추가**: 해당 리뷰와 연결된 상품 정보도 모델에 추가합니다.
		AddItem product = review.getAddItem();

		// 상품 이미지 리스트 가져오기
		List<String> imageUrls = productImageRepository.findByAddItemId(product.getId()).stream()
				.map(ProductImage::getImageUrl).collect(Collectors.toList());

		model.addAttribute("imageUrls", imageUrls); // 최신 이미지 리스트 추가
		model.addAttribute("review", review);
		model.addAttribute("reviewImages", reviewImages);
		model.addAttribute("product", product);

		return "productDetail/productReviewEdit"; // 리뷰 수정 페이지
	}

	// 리뷰 수정
	@PostMapping("/review/{reviewId}/edit")
	public String editReview(@PathVariable Long reviewId, @RequestParam String content,
			@RequestParam(value = "imageUrls", required = false) List<String> imageUrls, Principal principal)
			throws AccessDeniedException {

		if (principal == null) {
			throw new IllegalArgumentException("로그인이 필요합니다.");
		}
		String username = principal.getName();
		detailService.editReview(reviewId, username, content, imageUrls);
		return "redirect:/productDetail/productDetail/" + reviewRepository.findById(reviewId)
				.orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다.")).getAddItem().getId();
	}

	@PostMapping("/review/{reviewId}/like")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Long reviewId, Principal principal) {

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
		Map<String, Object> response = new HashMap<>();
		response.put("likeCount", likeCount);
		response.put("liked", liked);

		return ResponseEntity.ok(response);
	}

}
