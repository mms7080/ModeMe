package com.example.Modeme.purchase.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Modeme.Config.CustomUserDetails;
import com.example.Modeme.Manager.Entity.AddItem;
import com.example.Modeme.Manager.ManagerRepository.AddItemRepository;
import com.example.Modeme.User.UserDTO.Headerlogin;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;
import com.example.Modeme.purchase.dao.PurchaseRepository;
import com.example.Modeme.purchase.dao.ShoppingCartRepository;
import com.example.Modeme.purchase.dto.Purchase;
import com.example.Modeme.purchase.dto.PurchaseItem;
import com.example.Modeme.purchase.dto.ShoppingCart;

@Controller
public class PurchaseController {
	@Autowired
	Headerlogin keep;
	
	@Autowired
	private AddItemRepository air;
	
	@Autowired
	private UserRepository ur;
	
	@Autowired
	private PurchaseRepository pr;
	
	@Autowired
	private ShoppingCartRepository scr;
	
	@ModelAttribute //모든 매핑에 추가할 코드
    public void addAttributes(Model model, Principal principal) {
        keep.headerlogin(model, principal); //로그인 유지 
    }
	
	@PostMapping("/cart/add")
	public ResponseEntity<String> addToCart(@RequestBody ShoppingCart cartItem, @AuthenticationPrincipal CustomUserDetails userDetails)	{
		System.out.println("장바구니추가");
		cartItem.setUserId(userDetails.getUser().getId());
		scr.save(cartItem);
		return ResponseEntity.ok("success");
	} 
	
	
	// 결제페이지
	@GetMapping("/purchase/{id}")
	public String purchase(Model model, @PathVariable Long id, Principal prin) {
		User u = ur.findByUsername(prin.getName()).get();
		Optional<AddItem> as = air.findById(id);
		AddItem a = as.get();
		model.addAttribute("user",u);
		PurchaseItem pi = new PurchaseItem();
		pi.setProductId(a.getId());
		pi.setPrice(a.getPrice());
		pi.setProductName(a.getName());
		model.addAttribute("items", pi);
//		model.addAttribute("aId", a.getId());
		return "/purchase/purchase";
	}
	
	// 결제페이지(장바구니에서 넘어올때)
	@PostMapping("/purchase")
	public String purchaseMultipleItems(
		@RequestParam List<Long> productId, // 폼으로 전송된 productId 리스트 받기
	    @RequestParam List<String> productName, // 폼으로 전송된 productName 리스트 받기
	    @RequestParam List<Integer> price, // 폼으로 전송된 price 리스트 받기
	    @RequestParam List<Integer> quantity, // 폼으로 전송된 quantity 리스트 받기
	    Principal prin,
	    Model model) {
		User u = ur.findByUsername(prin.getName()).get();
	    List<PurchaseItem> items = new ArrayList<>();
	    
	    for (int i = 0; i < productId.size(); i++) {
	        PurchaseItem item = new PurchaseItem();
	        item.setProductId(productId.get(i));
	        item.setProductName(productName.get(i));
	        item.setPrice(price.get(i));
	        item.setQuantity(quantity.get(i));
	        items.add(item);
	    }
	    // 모델에 유저 정보와 상품 목록을 추가
	    model.addAttribute("user", u);
	    model.addAttribute("items", items);  // 받은 items 리스트를 모델에 추가

	    // 결제 페이지로 리디렉션
	    return "/purchase/purchase";
	}

	
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
	
	@GetMapping("/insertPurchase")
	@ResponseBody
	public String insertPurchase(@RequestParam("aId") int aId, @RequestParam("userId") int uId,
			@RequestParam("address") String address, @RequestParam("addressDetail") String addrDetail,
			@RequestParam("totalPrice") int price, @RequestParam("impUid") String impUid,
			@RequestParam("merchantUid") String merchantUid, @RequestParam("itemname") String itemname,
			 @AuthenticationPrincipal CustomUserDetails userDetails) {
		String userid = userDetails.getUsername();
		
		Purchase p = new Purchase();
		p.setUserId(uId);
		p.setProductNumber(aId);
		p.setProductMany(1);
		p.setAddress(address);
		p.setAddressDetail(addrDetail);
		p.setItemname(itemname);
		p.setUsername(userid);
		p.setTotalPrice(price);
//		p.setImpUid(impUid);
//		p.setMerchantUid(merchantUid);
		
		pr.save(p);
		
		return "success";
	}
}
