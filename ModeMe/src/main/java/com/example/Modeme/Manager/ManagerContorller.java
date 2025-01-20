package com.example.Modeme.Manager;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    
//    @GetMapping("/manager/new")
//    public String addItemForm(Principal principal) {
//    	if (isAdmin(principal)) {
//    		return "/manager/managerInput";
//    	}
//    	return "redirect:/main";
//    }
    
    @GetMapping("/new")
    public String addItemForm(Model model) {
    	return "/manager/managerInput";
    }
    
//    @PostMapping("/manager/new")
//    public String addItem(@ModelAttribute AddItemDTO addItemDTO, Principal principal) {
//    	if(isAdmin(principal)) {
//    		as.addItemWithImages(addItemDTO);
//    		return "redirect:/managerProduct";
//    	}
//    	return "redirect:/managerProduct";
//    }
    @PostMapping("/new")
    public String addItem(@ModelAttribute AddItemDTO addItemDTO) {
            // 상품 등록 및 이미지 처리 서비스 호출
            AddItem savedItem = as.addItemWithImages(addItemDTO);
            
            // 상품 등록이 성공적으로 처리되었으면 콘솔에 출력
            System.out.println("등록된 상품: " + savedItem);
            
            // 상품 등록 완료 후 리다이렉트
            return "redirect:/manager/managerProduct";  // 상품 목록 페이지로 리다이렉트
            
        }
//    
//    @GetMapping("/managerProduct")
//    public String getProductList(Principal principal, Model model) {
//    	if(isAdmin(principal)) {
//    		List<AddItem> products = ar.findAll();
//            return "manager/managerProduct";
//    	}
//    		return "redirect:/main";
//    }
    
    @GetMapping("/managerProduct")
    public String getProductList(Model model) {
    	List<AddItem> products = ar.findAll();
    	model.addAttribute("products", products);
    	return "manager/managerProduct";
    }
    
//    
    private boolean isAdmin(Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            return "admin".equals(user.getRole());
        }
        return false;
    }
}

