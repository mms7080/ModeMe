	package com.example.Modeme.controller;
	
	import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
import com.example.Modeme.Mypage.MypageEntity.Wishlist;
import com.example.Modeme.Mypage.MypageRepository.AddressRepository;
import com.example.Modeme.Mypage.MypageRepository.DefaultaddressRepository;
import com.example.Modeme.Mypage.MypageRepository.WishlistRepository;
import com.example.Modeme.Mypage.MypageService.AddressService;
import com.example.Modeme.Mypage.MypageService.DefaultaddressService;
import com.example.Modeme.Mypage.MypageService.WishlistService;
import com.example.Modeme.User.UserDTO.Headerlogin;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;
	
	@Controller
	public class MypageController {
		
		   @Autowired
		   Headerlogin keep; // 로그인 유지 재사용 Headerlogin 클래스
		   
		   @Autowired
		   UserRepository userrep;
		   
		   @Autowired
		   AddressRepository addressrep;
		   @Autowired
		   AddressService addresser;
		   
		   @Autowired
		   DefaultaddressRepository defaultrep;
		   @Autowired
		   DefaultaddressService defaultser;
		   
		   @Autowired
		   WishlistRepository wishrep;
		   @Autowired
		   WishlistService wishser;
		   
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
			
			// 적립금
			@GetMapping("/mileage")
			public String Mileage(
					@AuthenticationPrincipal CustomUserDetails userDetails,
					Model model		
			) {
				String userid = userDetails.getUsername();
				
				Optional<User> user = userrep.findByUsername(userid);
				
				// Optional<User>에서 createdAt 값 추출
			    LocalDateTime userDate = user.map(User::getCreatedAt) // createdAt 필드 접근
			                                 .orElse(null); // 값이 없으면 null 처리
				
			 // 모델에 추가
			    model.addAttribute("mileage_list", userDate);
				
				return "/MyPage/mileage";
			}

			// 관심 상품
			@GetMapping("/wishlist")
			public String WishList(
					@AuthenticationPrincipal CustomUserDetails userDetails,
					Model model
			) {
				String userid = userDetails.getUsername();
			    
			    List<Wishlist> wishlist = wishrep.findByUserid(userid);

			    model.addAttribute("wishlist", wishlist);
			    return "/MyPage/wishlist";
			}
			
			@PostMapping("/wishlist_delete")
			public String DeleteWish(
					@AuthenticationPrincipal CustomUserDetails userDetails,
					@RequestParam(value="wishid") Long wishid,
					@RequestParam(value="action") String action
			) {
				String userid = userDetails.getUsername();
				
				if(action.equals("delete")) {
				
				wishser.deleteWishlist(userid, wishid);
				}
				else {
					return "/wishlist"; //장바구니 완료되면 insert랑 테이블 연결 시키기
				}
				
				return "redirect:/wishlist";
			}
	
			// 배송 주소록 관리
			@GetMapping("/address")
			public String Address(
				@AuthenticationPrincipal CustomUserDetails userDetails,
				@RequestParam(value = "page", defaultValue = "1") int page,
				Model model
			) {
				String userid = userDetails.getUsername();
				List<Address> address_list = addressrep.findByUserid(userid);
				
				model.addAttribute("address_list",address_list);
				
	List<Defaultaddress> default_list = defaultrep.findByUserid(userid);
				
				model.addAttribute("default_list",default_list);
				
				
				//페이지네이션
				// 사용자 ID로 주소 목록 조회
			    List<Address> address_page = addressrep.findByUserid(userid);
			    model.addAttribute("address_list", address_page);

			    // 페이지네이션 처리
			    List<Address> addresses = addressrep.findByUserid(userid); // 사용자 주소 목록

			    int pageSize = 5; // 한 페이지에 표시할 주소 수
			    int paginationSize = 10; // 페이지 번호 최대 표시 개수

			    // 데이터가 없다면 기본 페이지로 이동
			    if (addresses.isEmpty()) {
			        return "/MyPage/address"; // 기본 페이지로 이동
			    }

			    // 전체 페이지 개수 계산
			    int totalReservation = addresses.size();
			    int totalPages = (int) Math.ceil((double) totalReservation / pageSize);

			    // 현재 페이지 범위 계산
			    int startIndex = (page - 1) * pageSize;
			    int endIndex = Math.min(startIndex + pageSize, totalReservation);

			    // 현재 페이지에 해당하는 주소 목록
			    List<Address> paginationAddress = addresses.subList(startIndex, endIndex);

			    // 페이지네이션 범위 계산
			    int currentRangeStart = ((page - 1) / paginationSize) * paginationSize + 1;
			    int currentRangeEnd = Math.min(currentRangeStart + paginationSize - 1, totalPages);

			    // 모델에 페이지네이션 관련 데이터 추가
			    model.addAttribute("address_list", paginationAddress);
			    model.addAttribute("currentPage", page);
			    model.addAttribute("totalPages", totalPages);
			    model.addAttribute("startPage", currentRangeStart); 
			    model.addAttribute("endPage", currentRangeEnd);
				
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