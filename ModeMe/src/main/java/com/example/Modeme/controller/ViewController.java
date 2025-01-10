package com.example.Modeme.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

	//공지
	@GetMapping("/notice") 
	public String noticehome() {
		return "/Notice/NoticeHome";
	}
	//공지 작성
	@GetMapping("/noticeWrite") 
	public String noticeWrite() {
		return "/Notice/NoticeWrite";
	}
	//공지 상세페이지뷰
	@GetMapping("/noticeView") 
	public String noticeView() {
		return "/Notice/NoticeView";
	}
	//Q&A
	@GetMapping("/qna") 
	public String qnaHome() {
		return "/Notice/qnaHome";
	}
	//Q&A 작성
	@GetMapping("/qnaWrite") 
	public String qnaWrite() {
		return "/Notice/qnaWrite";
	}
	//Q&A 상세페이지뷰
	@GetMapping("/qnaView") 
	public String qnaView() {
		return "/Notice/qnaView";
	}
	//비밀글 페이지
	@GetMapping("/secretPage") 
	public String secretPage() {
		return "/Notice/secretPage";
	}
	//헤더 없어도되는데 나중에 쓸일있을거같아서 해둠
	@GetMapping("/header") 
	public String header() {
		return "/header";
	}
	//푸터 없어도되는데 나중에 쓸일있을거같아서 해둠
	@GetMapping("/footer") 
	public String footer() {
		return "/footer";
	}         
	
	//마이페이지
	@GetMapping("/mypage")
	public String MyPage() {
		return "/MyPage/MyPage";
	}
	
	//주문내역 조회
	@GetMapping("/order")
	public String Order() {
		return "/MyPage/order";
	}
	
	//관심 상품
	@GetMapping("/wishlist")
	public String WishList() {
		return "/MyPage/wishlist";
	}
	
	//적립금
	@GetMapping("/mileage")
	public String Mileage() {
		return "/MyPage/mileage";
	}
	
	//배송 주소록 관리
	@GetMapping("/address")
	public String Address() {
		return "/MyPage/address";
	}
	
	//제품 상세
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
	
	//결제페이지
	@GetMapping("/purchase")
	public String purchase() {
		return "/purchase/purchase";
	}
	//메인페이지
	@GetMapping("/")
	public String mainView() {
		return "/main";
	}
	@GetMapping("/main")
	public String mainView2() {
		return "/main";
	}
}
