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
import com.example.Modeme.Manager.ManagerService.AddItemService;
import com.example.Modeme.QnA.QnARepository.QnaRepository;
import com.example.Modeme.User.UserDTO.Headerlogin;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;
import com.example.Modeme.User.UserService.UserService;
import com.example.Modeme.prdDetail.service.ProductDetailService;

@Controller
@RequestMapping("/productDetail")
public class ProductDetailController {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AddItemRepository addItemRepository;

	@Autowired
	private QnaRepository qnaRepository;
	
	@Autowired
	private final UserService userService;

	@Autowired
	private final AddItemService addItemService;
	
	@Autowired
	private final ProductDetailService detailService;

	@Autowired
	private Headerlogin keep;

	// 생성자
	public ProductDetailController(ProductDetailService detailService, UserService userService, AddItemService addItemService) {
		this.addItemService = addItemService;
		this.detailService = detailService;
		this.userService = userService;
	}
	
	// 로그인
    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        keep.headerlogin(model, principal);

        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
        }
    }
    
//    // 상품 수정 페이지
//    @PostMapping("/productEdit/{id}")
//    public String getItemEditPage(@PathVariable Long id, @ModelAttribute AddItem updatedItem) {
//    	addItemService.addItemWithImages(id, updatedItem);
//        return "redirect:/productDetail";
//    }

	// 상품 상세 정보 조회
	@GetMapping("/productDetail/{id}")
	public String getProductDetail(@PathVariable Long id, Model model) {
		// 상품 정보 조회
		AddItem product = addItemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + id));

		// 모델에 상품 정보 추가
		model.addAttribute("product", product);

		// 선택한 사이즈를 개별적으로 표시하기 위해 List로 처리
		if (product.getProductSizes() != null && !product.getProductSizes().isEmpty()) {
			model.addAttribute("productSizes", product.getProductSizes());
		}

		return "productDetail/productDetail";
	}

}
