	package com.example.Modeme.controller;
	
	import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Modeme.Config.CustomUserDetails;
import com.example.Modeme.Mypage.MypageEntity.Address;
import com.example.Modeme.Mypage.MypageEntity.Defaultaddress;
import com.example.Modeme.Mypage.MypageRepository.AddressRepository;
import com.example.Modeme.Mypage.MypageRepository.DefaultaddressRepository;
import com.example.Modeme.Mypage.MypageService.AddressService;
import com.example.Modeme.Mypage.MypageService.DefaultaddressService;
import com.example.Modeme.User.UserDTO.Headerlogin;
	
	@Controller
	public class MypageController {
		
		   @Autowired
		   Headerlogin keep; // 로그인 유지 재사용 Headerlogin 클래스
		   
		   @Autowired
		   AddressRepository addressrep;
		   @Autowired
		   AddressService addresser;
		   
		   @Autowired
		   DefaultaddressRepository defaultrep;
		   @Autowired
		   DefaultaddressService defaultser;
		   
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
			public String Address(
				@AuthenticationPrincipal CustomUserDetails userDetails,
				Model model
			) {
				String userid = userDetails.getUsername();
				List<Address> address_list = addressrep.findByUserid(userid);
				
				model.addAttribute("address_list",address_list);
				
	List<Defaultaddress> default_list = defaultrep.findByUserid(userid);
				
				model.addAttribute("default_list",default_list);			
				
				return "/MyPage/address";
			}
			
			@PostMapping("/address")
			public String AddressPost(
				@AuthenticationPrincipal CustomUserDetails userDetails,
				@RequestParam(value = "recipient") String name,
				@RequestParam(value = "phone") String phone, @RequestParam(value = "zip") String zip,
				@RequestParam(value = "address") String address,
				@RequestParam(value = "extraaddress") String extraaddress,
				@RequestParam(value = "addressdetail") String addressdetail
			) {
				String userid = userDetails.getUsername();
				
				String full_address = zip + " " + address + extraaddress + " " + addressdetail;
				
				Address save_address = new Address(null,userid,name,phone,full_address);
				addressrep.save(save_address); //팝업에서 배송지목록 테이블로 저장
				
				return "redirect:/address";
			}
			
			// 기본 배송지 설정
		    @PostMapping("/address_default")
		    public String setDefaultAddress(
		        @AuthenticationPrincipal CustomUserDetails userDetails,
		        @RequestParam(value = "addressid") Long addressId,
		        @RequestParam(value = "recipient") String name,
				@RequestParam(value = "phone") String phone,
				@RequestParam(value = "address") String address
		    ) {
		        String userid = userDetails.getUsername();
		        
		        // 기존 기본 배송지 삭제 (필요시)
		        defaultser.deleteDefaultAddress(userid, addressId);
		        
		        Defaultaddress save_default = new Defaultaddress(null, userid, name, phone, address, true);
		        defaultrep.save(save_default);

		        return "redirect:/address"; // 처리 후 주소 목록 페이지로 리다이렉트
		    }
		    
		    //배송지목록(address 테이블) 삭제
		    @PostMapping("/address_delete")
		    public String DeleteAddress(
    		 @AuthenticationPrincipal CustomUserDetails userDetails,
		        @RequestParam(value = "addressid") Long addressId
		    ) {
		    	String userid = userDetails.getUsername();
		    	
		    	addresser.deleteAddress(userid, addressId);
		    	
		    	return "redirect:/address";
		    }
		    
		    @PostMapping("/default_delete")
		    public String DeleteDefault(
		    		@AuthenticationPrincipal CustomUserDetails userDetails,
			        @RequestParam(value = "addressid") Long addressId
		    		) {
		    	String userid = userDetails.getUsername();
		    	
		    	defaultser.deleteDefault(userid, addressId);
		    	
		    	return "redirect:/address";
		    }
		}