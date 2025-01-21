package com.example.Modeme.User.UserController;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.Modeme.User.UserDTO.Headerlogin;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final Headerlogin keep; // 로그인 유지 재사용 Headerlogin 클래스

    @ModelAttribute
    public void addAttributes(Model model, Principal principal) {
        keep.headerlogin(model, principal); // 로그인 유지
    }

    /** ======================== [ 로그인 ] ======================== **/

    // 로그인 페이지
    @GetMapping("/signin")
    public String signin() {
        return "/Sign/signin"; // 로그인 HTML 경로
    }

    /** ======================== [ 회원정보 찾기 ] ======================== **/

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

    /** ======================== [ 회원정보 수정 ] ======================== **/

    // 회원정보 수정 페이지
    @GetMapping("/modify")
    public String modify() {
        return "/Sign/modify"; // 회원정보 수정 HTML 경로
    }
}
