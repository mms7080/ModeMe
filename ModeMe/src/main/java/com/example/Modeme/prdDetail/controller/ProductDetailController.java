package com.example.Modeme.prdDetail.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Modeme.Manager.Entity.AddItem;
import com.example.Modeme.Manager.ManagerRepository.AddItemRepository;
import com.example.Modeme.QnA.QnARepository.QnaRepository;
import com.example.Modeme.User.UserDTO.Headerlogin;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;
import com.example.Modeme.User.UserService.UserService;
import com.example.Modeme.prdDetail.entity.ProductReview;
import com.example.Modeme.prdDetail.repository.ProductReviewRepository;
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
	private QnaRepository qnaRepository;
	
	@Autowired
	private final UserService userService;
	
	@Autowired
	private final ProductDetailService detailService;

	@Autowired
	private final ProductEditService editService;
	
	@Autowired
	private Headerlogin keep;

	// 생성자
	public ProductDetailController(ProductDetailService detailService, UserService userService, ProductEditService editService, ProductReviewRepository reviewRepository) {
		this.editService = editService;
		this.detailService = detailService;
		this.userService = userService;
		this.reviewRepository = reviewRepository;
	}
	
	// 로그인 정보유지
    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        keep.headerlogin(model, principal);

        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
    }
    
    // 관리자 권한 확인 헬퍼 메서드
    private boolean isAdmin(Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            return "admin".equals(user.getRole());
        }
        return false;
    }
    
    
    // 상품 수정 페이지 렌더링
    @GetMapping("/productEdit/{id}")
    public String getProductEditPage(@PathVariable Long id, Principal principal, Model model) {
        AddItem item = editService.findById(id);
        if (item == null) {
            throw new IllegalArgumentException("해당 상품을 찾을 수 없습니다. ID: " + id);
        }
        model.addAttribute("product", item);
        return "/productDetail/productEdit";
    }

    // 상품 수정 처리
    @PostMapping("/productEdit/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute AddItem updatedItem, Principal principal) {
        try {
        	System.out.println("Updated Item: " + updatedItem);
            editService.updateItem(id, updatedItem);
            return "redirect:/productDetail/" + id;
        } catch (Exception e) {
            // 에러 처리 로직
            e.printStackTrace();
            return "redirect:/productEdit/" + id + "?error=true";
        }
    }

	// 상품 상세 정보 조회
	@GetMapping("/productDetail/{id}")
	public String getProductDetail(@PathVariable Long id, Model model, Principal principal) {
		// 상품 정보 조회
		AddItem product = addItemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + id));

		// 모델에 상품 정보 추가
		model.addAttribute("product", product);

		// 선택한 사이즈를 개별적으로 표시하기 위해 List로 처리
		if (product.getProductSizes() != null && !product.getProductSizes().isEmpty()) {
			model.addAttribute("productSizes", product.getProductSizes());
		}

		return "/productDetail/productDetail";
	}
	
	@GetMapping("/{id}/review")
	public String reviewWritePage(@PathVariable Long id, Model model, Principal principal) {
	    System.out.println("Requested AddItem ID: " + id);
	    AddItem addItem = addItemRepository.findById(id).orElseThrow(() ->
	    new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + id));
	    model.addAttribute("product", addItem);
	    return "/productDetail/productReviewWrite";
	}


    // 리뷰 저장
    @PostMapping("/{id}/review")
    public String saveReview(@PathVariable Long id, @ModelAttribute ProductReview review, Principal principal) {
        detailService.saveReview(id, review, principal.getName());
        return "redirect:/productDetail/" + id;
    }
    
    

}
