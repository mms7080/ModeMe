package com.example.Modeme.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.Modeme.User.UserDTO.Headerlogin;

@Controller
public class MypageController {
	
	   @Autowired
	   Headerlogin keep; // 로그인 유지 재사용 Headerlogin 클래스
	   
	    @ModelAttribute //모든 매핑에 추가할 코드
	    public void addAttributes(Model model, Principal principal) {
	        keep.headerlogin(model, principal); //로그인 유지 
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
		
}

