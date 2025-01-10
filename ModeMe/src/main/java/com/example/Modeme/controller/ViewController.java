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
	@GetMapping("/MyPage")
	public String MyPage() {
		return "/MyPage/MyPage";
	}
	
	//제품 상세
	@GetMapping("/productDetail")
	public String productDetail() {
		return "/productDetail/productDetail";
	}
	
	//결제페이지
	@GetMapping("/purchase")
	public String purchase() {
		return "purchase/purchase";
	}
	//메인페이지
	@GetMapping("/")
	public String mainView() {
		return "main";
	}
	@GetMapping("/main")
	public String mainView2() {
		return "main";
	}
}
