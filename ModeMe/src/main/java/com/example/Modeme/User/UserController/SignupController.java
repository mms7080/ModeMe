package com.example.Modeme.User.UserController;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.Modeme.User.UserDTO.Headerlogin;
import com.example.Modeme.User.UserDTO.UserDTO;
import com.example.Modeme.User.UserService.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SignupController {

    @Autowired
    Headerlogin keep; // 로그인 유지 재사용 Headerlogin 클래스

    private final UserService userService;

    @ModelAttribute // 모든 매핑에 추가할 코드
    public void addAttributes(Model model, Principal principal) {
        keep.headerlogin(model, principal); // 로그인 유지
    }

    // 회원가입 폼
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "/Sign/signup"; // 회원가입 HTML 경로
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signupProcess(@Valid UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/Sign/signup"; // 유효성 오류가 있으면 다시 폼으로 이동
        }

        try {
            // 전화번호 합치기 (프론트에서 3개 입력 받아 1개로 합친 후 UserDTO에 전달됨)
            userDTO.setPhone(userDTO.getPhone1() + userDTO.getPhone2() + userDTO.getPhone3());

            // 회원가입 처리
            userService.registerUser(userDTO);

            // 회원가입 성공 시 ?success 파라미터 추가하여 리다이렉트
            return "redirect:/signup?success";

        } catch (IllegalArgumentException e) {
            // 회원가입 실패 시 ?error 파라미터 추가하여 리다이렉트
            return "redirect:/signup?error";
        }
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
