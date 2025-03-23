package com.example.Modeme.Manager;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Modeme.Manager.Entity.AddItem;
import com.example.Modeme.Manager.ManagerDTO.AddItemDTO;
import com.example.Modeme.Manager.ManagerDTO.ProductSaleDTO;
import com.example.Modeme.Manager.ManagerDTO.UserDataDTO;
import com.example.Modeme.Manager.ManagerRepository.AddItemRepository;
import com.example.Modeme.Manager.ManagerRepository.ProductImageRepository;
import com.example.Modeme.Manager.ManagerService.AddItemService;
import com.example.Modeme.Manager.ManagerService.ManagerReviewService;
import com.example.Modeme.Manager.ManagerService.ManagerSaleService;
import com.example.Modeme.QnA.QnARepository.QnaRepository;
import com.example.Modeme.User.UserDTO.Headerlogin;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;
import com.example.Modeme.prdDetail.entity.ProductReview;
import com.example.Modeme.prdDetail.repository.ProductReviewRepository;
import com.example.Modeme.purchase.dao.PurchaseRepository;


@Controller
public class ManagerContorller {
	
	@Autowired
	private PurchaseRepository pr;
	
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddItemRepository ar;
    
    @Autowired
    private ProductReviewRepository prr;
    
    @Autowired
    private ManagerReviewService mrs;

    
    @Autowired
    private ManagerSaleService mss;

    @Autowired
    private QnaRepository qnaRepository;
    
    @Autowired
    private AddItemService as;

    @Autowired
    private Headerlogin keep;

    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        keep.headerlogin(model, principal); // 로그인 정보 헤더에 추가
    }

    // 관리자 메인
    @GetMapping("manager/managerMain")
    public String adminDashboard(Model model, Principal principal) {
    	if(principal != null) {
    		String username = principal.getName(); // 로그인한 사용자명 가져오기
    		model.addAttribute("username", username); // 모델에 사용자명 추가
    	}
        // 총 회원 수
        long totalUsers = userRepository.count();

        // QnA 글 수
        long totalQnAs = qnaRepository.count();
        
        // 총 등록된 상품 수
        long toatalItems  = ar.count();
        
        // 총 등록된 리뷰 수
        long totalReviews  = prr.count();

        // 모델에 데이터 추가
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalQnAs", totalQnAs);
        model.addAttribute("totalItems", toatalItems);
        model.addAttribute("totalReviews", totalReviews);

        return "manager/managerMain";
    }
    

    // 상품 등록 폼
    @GetMapping("manager/new")
    public String addItemForm(Model model, Principal principal) {
    	if(principal != null) {
    		String username = principal.getName();
    		model.addAttribute("username", username);	// 모델에 사용자명 추가
    	}
        model.addAttribute("addItemDTO", new AddItemDTO());	// 상품 등록 폼을 위한 DTO 추가
        return "manager/managerInput";
    }

    // 상품 등록
    @PostMapping("manager/new")
    public String addItem(@ModelAttribute AddItemDTO addItemDTO, Principal principal,
    		@RequestParam(value = "imageUrls", required = false) List<String> imageUrls) {
    	
    	// 이미지 URL이 없으면 빈 리스트로 처리
    	if(principal != null) {
    		String username = principal.getName();
    		System.out.println("상품 삭제한 사용자:" + username);
    	}	
    	 if (imageUrls == null || imageUrls.isEmpty()) {
    	        System.out.println("🚨 imageUrls 값이 없습니다! 빈 리스트로 처리합니다.");
    	        imageUrls = new ArrayList<>(); // 빈 리스트로 초기화
    	    }
        // 상품 등록 및 저장
    	addItemDTO.setImageUrls(imageUrls);
        AddItem savedItem = as.addItemWithImages(addItemDTO);

        // 상품 등록 후 상세 페이지로 리다이렉트
        return "redirect:/productDetail/productDetail/" + savedItem.getId();
    }

    @GetMapping("manager/managerProduct")
    public String getProductList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(required = false) String option,
        @RequestParam(required = false) String keyword,
        Model model, Principal principal
    ) {
        if (principal != null) {
            String username = principal.getName();
            model.addAttribute("username", username); // 모델에 사용자명 추가
        }

        // keyword가 null인 경우 빈 문자열로 설정
        if (keyword == null) {
            keyword = "";
        }

        // 페이지네이션을 고려한 상품 목록 조회
        Page<AddItem> productPage;

        // 검색 조건이 있을 경우 검색된 상품을 가져오고, 없으면 기본 목록을 가져옴
        if (option != null && keyword != null && !keyword.isBlank()) {
            productPage = as.searchProducts(option, keyword, PageRequest.of(page, size));
        } else {
            productPage = as.getProducts(PageRequest.of(page, size));
        }

        List<AddItem> products = productPage.getContent();
        int totalPages = productPage.getTotalPages();

        // 최신 이미지 URL 적용
        List<String> firstImageUrls = new ArrayList<>();
        for (AddItem product : products) {
            // 최신 이미지 목록 조회
            List<String> latestImages = as.getLatestImageUrls(product.getId());

            // 최신 이미지 설정
            product.setImageUrls(latestImages);

            // 첫 번째 이미지 URL 저장
            firstImageUrls.add(latestImages.isEmpty() ? null : latestImages.get(0));
        }

        // 모델에 데이터를 추가
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);
        model.addAttribute("option", option);
        model.addAttribute("keyword", keyword);
        model.addAttribute("firstImageUrls", firstImageUrls);

        return "manager/managerProduct";  // 상품 관리 페이지 반환
    }

    
 // 상품 삭제 처리
    @DeleteMapping("manager/deleteProduct/{id}")
    @ResponseBody
    public String deleteProduct(@PathVariable Long id, Principal principal) {
       	if(principal != null) {
    		String username = principal.getName();
    		System.out.println("상품 삭제한 사용자:" + username); // 상품 삭제한 사용자 정보 출력
    	}
        try {
            as.deleteProduct(id); // 상품 삭제
            return "상품 삭제 성공";
        } catch (Exception e) {
            e.printStackTrace();
            return "상품 삭제 실패";
        }
    }   
    
    // 상품 리뷰 목록 페이지
    @GetMapping("manager/managerReview")
    public String getReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String option,
            @RequestParam(required = false) String keyword,
            Model model, Principal principal) {

        // keyword가 null인 경우 빈 문자열로 설정
        if (keyword == null) {
            keyword = "";
        }

        // 페이지네이션 설정
        Pageable pageable = PageRequest.of(page, size);

        Page<ProductReview> productReviewPage;

        if (option != null && !keyword.isBlank()) {
            // 검색 호출
            productReviewPage = mrs.searchReviews(option, keyword, pageable);
        } else {
            // 기본 목록 호출
            productReviewPage = mrs.getReviews(pageable);
        }

        List<ProductReview> productReviews = productReviewPage.getContent();
        int totalPages = productReviewPage.getTotalPages();

        // 모델에 데이터 추가
        model.addAttribute("productReviews", productReviews);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);
        model.addAttribute("option", option);
        model.addAttribute("keyword", keyword);

        return "manager/managerReview";  // 리뷰 관리 페이지 반환
    }

    // 리뷰 삭제 처리
    @DeleteMapping("manager/deleteReview/{id}")
    @ResponseBody
    public String deleteReview(@PathVariable Long id) {
        try {
            prr.deleteById(id); // 리뷰 삭제
            return "리뷰 삭제 성공";
        } catch (Exception e) {
            e.printStackTrace();
            return "리뷰 삭제 실패";
        }
    }
    
    // 사용자 관리 페이지
    @GetMapping("manager/users")
    public String getUserManagementPage(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "5") int size, 
            @RequestParam(required = false) String option, 
            @RequestParam(required = false) String keyword, 
            Model model, Principal principal) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("id")));  // 내림차순 정렬 추가
        Page<User> userPage;

        // role 검색시 '관리자' -> 'admin', '일반' -> 'user'로 변환
        if ("role".equals(option) && keyword != null) {
            if ("관리자".equals(keyword)) {
                keyword = "admin"; // '관리자'를 'admin'으로 변환
            } else if ("일반".equals(keyword)) {
                keyword = "user"; // '일반'을 'user'로 변환
            }
        }

        // 검색 옵션에 따라 사용자 검색
        if ("username".equals(option)) {
            userPage = userRepository.findByUsernameContaining(keyword, pageable);
        } else if ("role".equals(option)) {
            userPage = userRepository.findByRoleContaining(keyword, pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        // User -> UserDataDTO 변환
        Page<UserDataDTO> userDtoPage = userPage.map(user -> {
            Long qnaCount = qnaRepository.countByUser(user);
            Long reviewCount = prr.countByUsers(user);
            return new UserDataDTO(user, qnaCount, reviewCount);
        });

        model.addAttribute("users", userDtoPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userDtoPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("option", option);
        model.addAttribute("keyword", keyword);

        return "manager/managerUser";	// 사용자 관리 페이지 반환
    }

    // 판매 관리 페이지
    @GetMapping("manager/ManagerSale")
    public String getSaleData(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(required = false) String newProcess,
        @RequestParam(required = false) String searchOption,
        @RequestParam(required = false) String keyword,
        Model model, Principal principal) {

        // 페이지 네이션 처리: 페이지와 사이즈 값을 Pageable 객체로 생성
        Pageable pageable = PageRequest.of(page, size);

        // 판매 데이터를 가져오기 (검색 옵션과 검색어를 필터링에 적용)
        Page<ProductSaleDTO> saleData = mss.getSaleData(pageable, newProcess, searchOption, keyword);

        // 월별 판매 금액 데이터를 가져오기
        Map<String, Integer> salesByMonth = mss.getSalesByMonth();

        // 카테고리별 판매 금액 데이터를 가져오기
        Map<String, Integer> salesByCategory = mss.getSalesByCategory();

        // 주문 상태별 개수 데이터를 가져오기
        Map<String, Long> orderCountByProcess = mss.getOrderCountByProcess();

        // 모델에 데이터 추가 (뷰에서 사용할 수 있도록 전달)
        model.addAttribute("saleData", saleData);
        model.addAttribute("salesByMonth", salesByMonth);
        model.addAttribute("salesByCategory", salesByCategory);
        model.addAttribute("orderCountByProcess", orderCountByProcess);

        // 페이지네이션 정보
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", saleData.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("searchOption", searchOption); // 검색 옵션 추가
        model.addAttribute("keyword", keyword); // 검색어 추가

        return "manager/managerSale"; // 판매 관리 페이지 반환
    }
    
    // 판매 상태 업데이트 처리
    @PutMapping("manager/ManagerSale/{saleId}")
    @ResponseBody
    public ResponseEntity<String> updateSaleProcess(
        @PathVariable Long saleId,
        @RequestBody Map<String, String> requestData) {

        String newProcess = requestData.get("newProcess");

        // newProcess 값이 없는 경우 예외 처리
        if (newProcess == null || newProcess.isEmpty()) {
            return ResponseEntity.badRequest().body("변경할 주문 상태를 입력해주세요.");
        }

        try {
            String resultMessage = mss.updateSaleProcess(saleId, newProcess);
            return ResponseEntity.ok(resultMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }

    // 주문 삭제 처리
    @DeleteMapping("manager/ManagerSale/{saleId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long saleId) {
        try {
            pr.deleteById(saleId);
            return ResponseEntity.ok("주문이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("주문 삭제 중 오류 발생");
        }
    }

} 