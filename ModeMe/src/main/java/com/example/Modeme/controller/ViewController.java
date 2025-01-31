package com.example.Modeme.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.Modeme.Manager.Entity.AddItem;
import com.example.Modeme.Manager.ManagerRepository.AddItemRepository;
import com.example.Modeme.User.UserDTO.Headerlogin;
import com.example.Modeme.User.UserRepository.UserRepository;
import com.example.Modeme.prdDetail.repository.ProductDetailRepository;
import com.example.Modeme.prdDetail.service.ProductEditService;

@Controller
public class ViewController {
	@Autowired
	Headerlogin keep; // 로그인 유지 재사용 Headerlogin 클래스
	
	@Autowired
	private UserRepository ur;
	
	@Autowired
	private AddItemRepository air;
	
	@Autowired
	private ProductDetailRepository pdr;
	
	@Autowired
	private ProductEditService pes;
	
    @ModelAttribute //모든 매핑에 추가할 코드
    public void addAttributes(Model model, Principal principal) {
        keep.headerlogin(model, principal); //로그인 유지 
    }
    
	@GetMapping("/header")
	public String getHeader(Model model, Principal principal) { // principal -> 현재 인증된 사용자의 정보 담고있는 객체
		if (principal != null) {
			// 사용자가 로그인한 경우
			model.addAttribute("loggedIn", true);
			model.addAttribute("username", principal.getName());
		} else {
			// 사용자가 로그인하지 않은 경우
			model.addAttribute("loggedIn", false);
		}
		return "header";
	}

	// 푸터 없어도되는데 나중에 쓸일있을거같아서 해둠
	@GetMapping("/footer")
	public String footer() {
		return "/footer";
	}
	// 장바구니
	@GetMapping("/shoppingcart")
	public String shoppingcart() {
		
		return "/purchase/shoppingCart";
	}

	// 제품 상세
	@GetMapping("/productDetail")
	public String productDetail() {
		return "/productDetail/productDetail";
	}

	// 제품 리뷰 작성페이지
	@GetMapping("/productReviewWrite")
	public String productReviewWrite() {
		return "/productDetail/productReviewWrite";
	}

	// 제품 문의 작성페이지
	@GetMapping("/productQAWrite")
	public String productQAWrite() {
		return "/productDetail/productQAWrite";
	}
	
	// 제품 문의 작성페이지
	@GetMapping("/productEdit")
	public String productEdit() {
		return "/productDetail/productEdit";
	}


	// 메인페이지
	@GetMapping("/")
	public String mainView(Model model) {
		List<AddItem> aList = air.findAll();
		model.addAttribute("aList", aList);
		return "/main";
	}

	@GetMapping("/main")
	public String mainView2(Model model) {
		List<AddItem> aList = air.findAll();
		model.addAttribute("aList", aList);
		return "/main";
	}
	
	// OUTER 예시 링크
	@GetMapping("/outer")
	public String outer() {
		return "/product/productList";
	}
	
	// CART 장바구니
	@GetMapping("/shopcart")
	public String shoppingCart() {
		return "/purchase/shoppingCart";
	}
	
	// 관리자 상품 등록
	@GetMapping("/managerInput")
	public String inputProduct() {
		return "/manager/managerInput";
	}

	// 관리자 주문 관리
	@GetMapping("/managerSale")
	public String manageSale() {
		return "/manager/managerSale";
	}

	// 관리자 상품 관리
	@GetMapping("/managerProduct")
	public String manageProduct() {
		return "/manager/managerProduct";
	}

	// 관리자 리뷰 관리
	@GetMapping("/managerReview")
	public String manageReview() {
		return "/manager/managerReview";
	}

}
