package com.example.Modeme.purchase.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import jakarta.transaction.Transactional;

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
	public ResponseEntity<String> addToCart(@RequestBody ShoppingCart cartItem, @AuthenticationPrincipal CustomUserDetails userDetails) {
	    
	    Long userId = userDetails.getUser().getId();
	    Long productId = cartItem.getProductId();
	    
	    // 동일한 상품이 장바구니에 있는지 확인
	    Optional<ShoppingCart> existingItem = scr.findByUserIdAndProductId(userId, productId);

	    if (existingItem.isPresent()) {
	        return ResponseEntity.ok("exists"); // 이미 존재하는 경우 저장하지 않음
	    }

	    // 장바구니에 추가
	    cartItem.setUserId(userId);
	    scr.save(cartItem);

	    return ResponseEntity.ok("success");
	}
	
	@PostMapping("/cart/delete")
	public ResponseEntity<String> deleteCartItem(@RequestParam Long productId, 
            @AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getUser().getId();
		
		// 해당 상품이 존재하는지 확인
		Optional<ShoppingCart> cartItem = scr.findByUserIdAndProductId(userId, productId);
		
		if (cartItem.isPresent()) {
			scr.delete(cartItem.get());
			return ResponseEntity.ok("deleted");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
		}
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
		pi.setQuantity(1);
		pi.setImageUrl(a.getImageUrls().get(0));
		List<PurchaseItem> piList = new ArrayList<>(); // 리스트형태로 model
		piList.add(pi);
		model.addAttribute("items", piList);
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
	    @RequestParam List<String> imageSrc, // 폼으로 전송된 imageSrc 리스트 받기
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
	        item.setImageUrl(imageSrc.get(i));
	        System.out.println(imageSrc.get(i));
	        items.add(item);
	    }
	    // 모델에 유저 정보와 상품 목록을 추가
	    model.addAttribute("user", u);
	    model.addAttribute("items", items);  // 받은 items 리스트를 모델에 추가

	    // 결제 페이지로 리디렉션
	    return "/purchase/purchase";
	}

	// 무통장입금을 선택했을 경우
	@GetMapping("/bankTransfer")
	public String bankTransfer(Principal prin, Model model) {
		User u = ur.findByUsername(prin.getName()).get();
//		List<Purchase> pList = pr.findByUserIdAndProcess(u.getId(), "입금전"); // Process 입금전, 입금완료, 배송중? 배송완료? 머 아무튼
//		근데 방금 주문한거만 보여주고싶은데 어떻게 하지
//		model.addAttribute("items", pList);
		return "/purchase/guideBankAccount";
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
	
	
	@GetMapping("/insertPurchase")
	@ResponseBody
	@Transactional // ✅ 트랜잭션 보장
	public String insertPurchase(@RequestParam("aId") String aIds, 
	                             @RequestParam("userId") int uId,
	                             @RequestParam("address") String address, 
	                             @RequestParam("addressDetail") String addrDetail,
	                             @RequestParam("prices") String price, 
	                             @RequestParam("impUid") String impUid,
	                             @RequestParam("merchantUid") String merchantUid, 
	                             @RequestParam("itemname") String itemnames,
	                             @RequestParam("quantities") String quantities, // ✅ 수량 추가
	                             Principal prin) {
	    User u = ur.findByUsername(prin.getName()).get();
	    String userid = u.getUsername();

	    // impUid == null 이면 무통장 입금 임. 이때 표시될 항목? 결제 전? 뭐 그렇게 뜨게 해야 할거같음
	    
	    String[] itemNamesArray = itemnames.split(",");
	    String[] aIdArray = aIds.split(",");
	    String[] quantityArray = quantities.split(",");
	    String[] priceArray = price.split(",");


	    // ✅ 상품 정보 검증
	    if (aIdArray.length == 0 || itemNamesArray.length == 0 || quantityArray.length == 0) {
	        return "error: 상품 정보 없음";
	    }


	    // ✅ 개별 상품 저장
	    for (int i = 0; i < itemNamesArray.length; i++) {
	        try {
	            int productId = Integer.parseInt(aIdArray[i].trim());
	            int quantity = Integer.parseInt(quantityArray[i].trim()); // ✅ 수량 변환
	            int itemPrice = Integer.parseInt(priceArray[i]); // ✅ 개별 상품 가격 계산

	            Purchase p = new Purchase();
	            p.setUserId(uId); // user pk
	            p.setProductNumber(productId);
	            p.setProductMany(quantity); // ✅ 실제 개별 수량 반영
	            p.setAddress(address);
	            p.setAddressDetail(addrDetail);
	            p.setItemname(itemNamesArray[i].trim());
	            p.setUsername(userid); // user id (=유저 로그인용 아이디)
	            p.setTotalPrice(itemPrice * quantity); // ✅ 수량 반영한 가격 저장

	            pr.save(p);
	            
	            scr.deleteByUserIdAndProductId(Long.valueOf(uId), Long.valueOf(productId));
	        } catch (Exception e) {
	            return "error: 저장 실패";
	        }
	    }


	    return "success";
	}



}
