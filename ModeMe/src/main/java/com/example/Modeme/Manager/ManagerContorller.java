package com.example.Modeme.Manager;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Modeme.Manager.Entity.AddItem;
import com.example.Modeme.Manager.ManagerDTO.AddItemDTO;
import com.example.Modeme.Manager.ManagerDTO.ProductSaleDTO;
import com.example.Modeme.Manager.ManagerDTO.UserDataDTO;
import com.example.Modeme.Manager.ManagerRepository.AddItemRepository;
import com.example.Modeme.Manager.ManagerService.AddItemService;
import com.example.Modeme.Manager.ManagerService.ManagerReviewService;
import com.example.Modeme.Manager.ManagerService.ManagerSaleService;
import com.example.Modeme.Manager.ManagerService.ManagerUserService;
import com.example.Modeme.QnA.QnARepository.QnaRepository;
import com.example.Modeme.User.UserDTO.Headerlogin;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;
import com.example.Modeme.prdDetail.entity.ProductReview;
import com.example.Modeme.prdDetail.repository.ProductReviewRepository;

@Controller
public class ManagerContorller {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddItemRepository ar;
    
    @Autowired
    private ProductReviewRepository prs;
    
    @Autowired
    private ManagerReviewService mrs;

    @Autowired
    private ManagerUserService mus;
    
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
        keep.headerlogin(model, principal);
    }

    // 관리자 메인
    @GetMapping("/manager/managerMain")
    public String adminDashboard(Model model, Principal principal) {
    	if(principal != null) {
    		String username = principal.getName();
    		model.addAttribute("username", username);
    	}
        // 총 회원 수
        long totalUsers = userRepository.count();

        // QnA 글 수
        long totalQnAs = qnaRepository.count();
        
        // 총 등록된 상품 수
        long toatalItems  = ar.count();
        
        // 총 등록된 리뷰 수
        long totalReviews  = prs.count();

        // 모델에 데이터 추가
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalQnAs", totalQnAs);
        model.addAttribute("totalItems", toatalItems);
        model.addAttribute("totalReviews", totalReviews);

        return "manager/managerMain";
    }
    

    // 상품 등록 폼
    @GetMapping("/manager/new")
    public String addItemForm(Model model, Principal principal) {
    	if(principal != null) {
    		String username = principal.getName();
    		model.addAttribute("username", username);
    	}
        model.addAttribute("addItemDTO", new AddItemDTO());
        return "/manager/managerInput";
    }

    // 상품 등록
    @PostMapping("/manager/new")
    public String addItem(@ModelAttribute AddItemDTO addItemDTO, Principal principal) {
    	
    	if(principal != null) {
    		String username = principal.getName();
    		System.out.println("상품 삭제한 사용자:" + username);
    	}
        // 상품 등록 및 저장
        AddItem savedItem = as.addItemWithImages(addItemDTO);

        // 상품 등록 후 상세 페이지로 리다이렉트
        return "redirect:/productDetail/productDetail/" + savedItem.getId();
    }

    @GetMapping("/manager/managerProduct")
    public String getProductList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(required = false) String option,
        @RequestParam(required = false) String keyword,
        Model model, Principal principal
    ) {
    	if(principal != null) {
    		String username = principal.getName();
    		model.addAttribute("username", username);
    	}
    	
        // 페이지네이션과 검색 조건을 고려한 상품 목록 조회
        Page<AddItem> productPage;
        
        if (option != null && keyword != null && !keyword.isBlank()) {
            // 검색 호출
            productPage = as.searchProducts(option, keyword, PageRequest.of(page, size));
        } else {
            // 기본 목록 호출
            productPage = as.getProducts(PageRequest.of(page, size));
        }

        List<AddItem> products = productPage.getContent();
        int totalPages = productPage.getTotalPages();

        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);
        model.addAttribute("option", option);
        model.addAttribute("keyword", keyword);

        return "manager/managerProduct";  // 상품 관리 페이지 반환
    }
    
 // 상품 삭제 처리
    @DeleteMapping("/manager/deleteProduct/{id}")
    @ResponseBody
    public String deleteProduct(@PathVariable Long id, Principal principal) {
       	if(principal != null) {
    		String username = principal.getName();
    		System.out.println("상품 삭제한 사용자:" + username);
    	}
        try {
            as.deleteProduct(id);
            return "상품 삭제 성공";
        } catch (Exception e) {
            e.printStackTrace();
            return "상품 삭제 실패";
        }
    }   
    
    @GetMapping("/manager/managerReview")
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
    @DeleteMapping("/manager/deleteReview/{id}")
    @ResponseBody
    public String deleteReview(@PathVariable Long id) {
        try {
            prs.deleteById(id); // 리뷰 삭제
            return "리뷰 삭제 성공";
        } catch (Exception e) {
            e.printStackTrace();
            return "리뷰 삭제 실패";
        }
    }
    
    // 모든 회원 정보 검색
    @GetMapping("/manager/users")
    public String getUserManagementPage(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "5") int size, 
            @RequestParam(required = false) String option, 
            @RequestParam(required = false) String keyword, 
            Model model, Principal principal) {

        Pageable pageable = PageRequest.of(page, size);
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
            Long reviewCount = prs.countByUsers(user);
            return new UserDataDTO(user, qnaCount, reviewCount);
        });

        model.addAttribute("users", userDtoPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userDtoPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("option", option);
        model.addAttribute("keyword", keyword);

        return "manager/managerUser";
    }
    
    @GetMapping("/manager/ManagerSale")
    public String getSaleData(
        @RequestParam(defaultValue = "0") int page,  // 기본값 0으로 설정
        @RequestParam(defaultValue = "5") int size,  // 기본값 5로 설정
        @RequestParam(required = false) String newProcess,  // 주문 상태 변경을 위한 newProcess 파라미터
        Model model, Principal principal) {

        // 페이지 네이션 처리: 페이지와 사이즈 값을 Pageable 객체로 생성
        Pageable pageable = PageRequest.of(page, size);

        // 주문 상태가 변경되어야 한다면, newProcess 파라미터에 따른 처리 (예: 'before', 'ready', 'delivery', 'done' 등)
        // 판매 데이터를 가져오기 (newProcess 파라미터가 null이 아닌 경우 상태를 반영)
        Page<ProductSaleDTO> saleData = mss.getSaleData(pageable, newProcess);

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
        model.addAttribute("orderCountByProcess", orderCountByProcess);  // 주문 상태별 개수 추가

        // 페이지네이션 정보 (현재 페이지와 총 페이지 수)
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", saleData.getTotalPages());
        model.addAttribute("pageSize", size);  // 페이지 크기 추가 (뷰에서 페이지 크기를 사용하려면 필요)

        return "manager/managerSale"; // 뷰 이름 반환
    }

    
    
}
