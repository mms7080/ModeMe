package com.example.Modeme.Manager;

import java.security.Principal;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Modeme.Manager.Entity.AddItem;
import com.example.Modeme.Manager.ManagerDTO.AddItemDTO;
import com.example.Modeme.Manager.ManagerRepository.AddItemRepository;
import com.example.Modeme.Manager.ManagerService.AddItemService;
import com.example.Modeme.QnA.QnARepository.QnaRepository;
import com.example.Modeme.User.UserDTO.Headerlogin;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;
import com.example.Modeme.User.UserService.UserService;

@Controller
public class ManagerContorller {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddItemRepository ar;

    @Autowired
    private UserService userService;

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
    public String adminDashboard(Model model) {
        // 총 회원 수
        long totalUsers = userRepository.count();

        // QnA 글 수
        long totalQnAs = qnaRepository.count();
        
        // 총 등록된 상품 수
        long toatalItems  = ar.count();

        // 모델에 데이터 추가
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalQnAs", totalQnAs);
        model.addAttribute("totalItems", toatalItems);

        return "manager/managerMain";
    }
    

    // 상품 등록 폼
    @GetMapping("/manager/new")
    public String addItemForm(Model model) {
        model.addAttribute("addItemDTO", new AddItemDTO());
        return "/manager/managerInput";
    }

    // 상품 등록
    @PostMapping("/manager/new")
    public String addItem(@ModelAttribute AddItemDTO addItemDTO) {
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
        Model model
    ) {
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
    public String deleteProduct(@PathVariable Long id) {
        try {
            as.deleteProduct(id);
            return "상품 삭제 성공";
        } catch (Exception e) {
            e.printStackTrace();
            return "상품 삭제 실패";
        }
    }
}
