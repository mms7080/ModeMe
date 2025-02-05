package com.example.Modeme.Manager;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.Modeme.Manager.Entity.AddItem;
import com.example.Modeme.Manager.ManagerService.AddItemService;

@Controller
public class SearchController {

	private final AddItemService addItemService;

    public SearchController(AddItemService addItemService) {
        this.addItemService = addItemService;
    }

    @GetMapping("/search")
    public String searchItems(@RequestParam String keyword, Model model) {
        List<AddItem> searchResults = addItemService.searchItems(keyword);
        model.addAttribute("aList", searchResults);
        model.addAttribute("searchKeyword", keyword); // 검색어 추가
        
     // 로그인 상태 확인 (Spring Security 사용)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser");
        model.addAttribute("loggedIn", isLoggedIn);
        return "/searchpage"; // 검색 결과를 Thymeleaf 템플릿(main.html)으로 전달
    }}