package com.example.Modeme.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
	// Q&A
	@GetMapping("/qna")
	public String qnaHome() {
		return "/Notice/qnaHome";
	}

	// Q&A 작성
	@GetMapping("/qnaWrite")
	public String qnaWrite() {
		return "/Notice/qnaWrite";
	}

	// Q&A 상세페이지뷰
	@GetMapping("/qnaView")
	public String qnaView() {
		return "/Notice/qnaView";
	}

	// 비밀글 페이지
	@GetMapping("/secretPage")
	public String secretPage() {
		return "/Notice/secretPage";
	}

	// 헤더 없어도되는데 나중에 쓸일있을거같아서 해둠
	@GetMapping("/header")
	public String header() {
		return "/header";
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
	// 마이페이지
	@GetMapping("/mypage")
	public String MyPage() {
		return "/MyPage/MyPage";
	}

	// 주문내역 조회
	@GetMapping("/order")
	public String Order() {
		return "/MyPage/order";
	}

	// 관심 상품
	@GetMapping("/wishlist")
	public String WishList() {
		return "/MyPage/wishlist";
	}

	// 적립금
	@GetMapping("/mileage")
	public String Mileage() {
		return "/MyPage/mileage";
	}

	// 배송 주소록 관리
	@GetMapping("/address")
	public String Address() {
		return "/MyPage/address";
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

	// 결제페이지
	@GetMapping("/purchase")
	public String purchase() {
		return "/purchase/purchase";
	}

	// 메인페이지
	@GetMapping("/")
	public String mainView() {
		return "/main";
	}

	@GetMapping("/main")
	public String mainView2() {
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
