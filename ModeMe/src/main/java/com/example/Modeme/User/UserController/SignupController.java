package com.example.Modeme.User.UserController;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.Modeme.User.UserDTO.Headerlogin;
import com.example.Modeme.User.UserDTO.UserDTO;
import com.example.Modeme.User.UserService.UserService;

import org.springframework.ui.Model;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class SignupController {
   
   @Autowired
   Headerlogin keep; // 로그인 유지 재사용 Headerlogin 클래스
   
    @ModelAttribute //모든 매핑에 추가할 코드
    public void addAttributes(Model model, Principal principal) {
        keep.headerlogin(model, principal); //로그인 유지 
    }

    private final UserService userService;

    // 회원가입 폼
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "/Sign/signup"; // 회원가입 HTML 경로
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signup(@Valid UserDTO userDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/Sign/signup"; // 유효성 오류가 있으면 다시 폼으로 이동
        }

        try {
            userService.registerUser(userDTO);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "signup"; // 오류 메시지와 함께 다시 폼으로 이동
        }

        return "redirect:/Sign/signin"; // 성공 시 로그인 페이지로 리다이렉트
    }

    // 로그인 페이지
    @GetMapping("/signin")
    public String signin() {
        return "/Sign/signin"; // 로그인 HTML 경로
    }

    // 아이디 찾기 페이지
    @GetMapping("/find_id")
    public String findId() {
        return "/Sign/find_id"; // 아이디 찾기 HTML 경로
    }

    // 비밀번호 찾기 페이지
    @GetMapping("/find_pw")
    public String findPw() {
        return "/Sign/find_pw"; // 비밀번호 찾기 HTML 경로
    }

    // 회원정보 수정 페이지
    @GetMapping("/modify")
    public String modify() {
        return "/Sign/modify"; // 회원정보 수정 HTML 경로
    }
}
