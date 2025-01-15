package com.example.Modeme.purchase.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Modeme.User.UserDTO.Headerlogin;

@Controller
public class PurchaseController {
	@Autowired
	Headerlogin keep;
	
	@ModelAttribute //모든 매핑에 추가할 코드
    public void addAttributes(Model model, Principal principal) {
        keep.headerlogin(model, principal); //로그인 유지 
    }
	
	/*
	@Autowired
	private ProductRepository pr;
	 */
	
	
	// ProductController 로 옮기면 좋음
	@GetMapping("/proList")
	public String productList(
			@RequestParam(defaultValue="1") int page,
			@RequestParam(defaultValue="all") String category
			) {
		/*
		if(category.equals("all"){
			List<Product> pList = pr.findAll();
		} else if(category.equals("outer"){
			List<Product> pList = pr.findByCategory(category);
		}
		// 등등 category 별로 찾아서 리스트 불러오기
		// 그 다음에 페이징처리도 섞기
		
		model.addAttribute("pList", pList);
		
		*/
		
		return "/product/productList";
	}
	
	@GetMapping("/purchaseAccept")
	public String purchaseAccept(
			@RequestParam("prodNum") List<Integer> pNums,
			@RequestParam("prodMany") List<Integer> pManys,
			@RequestParam(name="address", required=false, defaultValue="입력없음") String address,
			@RequestParam(name="addressDetail", required=false, defaultValue="입력없음") String addressDetail
			) {
		// 구매를 한 상품의 갯수만큼 purchase 객체 생성 & 정보 입력 후 디비에 저장
		return "redirect:/order"; // 결제 성공 후 주문내역으로 ㄱㄱ
	}
	
}
