package com.example.Modeme.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.Modeme.Config.CustomUserDetails;
import com.example.Modeme.Manager.Entity.AddItem;
import com.example.Modeme.Manager.ManagerRepository.AddItemRepository;
import com.example.Modeme.Mypage.MypageEntity.Wishlist;
import com.example.Modeme.Mypage.MypageRepository.WishlistRepository;
import com.example.Modeme.User.UserDTO.Headerlogin;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;
import com.example.Modeme.prdDetail.repository.ProductDetailRepository;
import com.example.Modeme.prdDetail.service.ProductEditService;
import com.example.Modeme.purchase.dao.ShoppingCartRepository;
import com.example.Modeme.purchase.dto.ShoppingCart;

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
	
	@Autowired
	private ShoppingCartRepository scr;
	
	@Autowired
	private WishlistRepository wishr;
	
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
	public String shoppingcart(Principal prin, Model model) {
	    User u = ur.findByUsername(prin.getName()).get();
	    List<ShoppingCart> sList = scr.findByUserId(u.getId()); // 유저가 담은 상품 목록 가져오기

	    Set<Long> productIdSet = new HashSet<>();
	    List<AddItem> itemList = new ArrayList<>();

	    for (ShoppingCart sc : sList) {
	        Long productId = sc.getProductId();
	        if (!productIdSet.contains(productId)) {
	            productIdSet.add(productId);

	            // Step 1: `colors` 먼저 가져오기
	            Optional<AddItem> itemWithColors = air.findWithColorsById(productId);
	            if (itemWithColors.isPresent()) {
	                AddItem addItem = itemWithColors.get();

	                // Step 2: `colorNames`도 가져오기 (기존 객체 업데이트)
	                Optional<AddItem> itemWithColorNames = air.findWithColorNamesById(productId);
	                itemWithColorNames.ifPresent(item -> addItem.setColorNames(item.getColorNames()));

	                itemList.add(addItem);
	            }
	        }
	    }

	    model.addAttribute("sList", sList);
	    model.addAttribute("itemList", itemList);
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
	public String mainView(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
	    List<AddItem> aList = air.findAll();
	    model.addAttribute("aList", aList);

	    // 로그인한 사용자가 있을 경우, 관심 상품 목록 가져오기
	    if (userDetails != null) {
	        String userId = userDetails.getUser().getUsername();
	        List<Wishlist> wList = wishr.findByUserid(userId);
	        model.addAttribute("wList", wList);
	    } else {
	        model.addAttribute("wList", null); // 비로그인 시 빈 리스트
	    }

	    return "/main";
	}

	
	// OUTER 예시 링크
	@GetMapping("/outer")
	public String outer() {
		return "/product/productList";
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
