package com.example.Modeme.Manager;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
@RequestMapping("/manager")
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
    Headerlogin keep;

    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        keep.headerlogin(model, principal);
    }
    
    //관리자 메인
    @GetMapping("/managerMain")
    public String adminDashboard(Model model) {
        // 총 회원 수
        long totalUsers = userRepository.count();

        // QnA 글 수
        long totalQnAs = qnaRepository.count();

        // 모델에 데이터 추가
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalQnAs", totalQnAs);

        return "manager/managerMain"; // 
    }
       
    @GetMapping("/new")
    public String addItemForm(Model model) {
    	return "/manager/managerInput";
    }
    
    @PostMapping("/new")
    public String addItem(@ModelAttribute AddItemDTO addItemDTO) {
        // 상품 등록 및 저장
        AddItem savedItem = as.addItemWithImages(addItemDTO);

        // 상품 등록 후 상세 페이지로 리다이렉트
        return "redirect:/manager/productDetail/" + savedItem.getId();
    }
    
    @GetMapping("/managerProduct")
    public String getProductList(Model model) {
    	List<AddItem> products = ar.findAll();
    	model.addAttribute("products", products);
    	return "manager/managerProduct";
    }
    
    @GetMapping("/productDetail/{id}")
    public String getProductDetail(@PathVariable Long id, Model model) {
        // 상품 정보 조회
        AddItem product = ar.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. ID: " + id));
        
        // 모델에 상품 정보 추가
        model.addAttribute("product", product);
        return "productDetail/productDetail";
    }
}

