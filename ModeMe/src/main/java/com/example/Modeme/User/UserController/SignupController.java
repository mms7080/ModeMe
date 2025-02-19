package com.example.Modeme.User.UserController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.Modeme.User.UserDTO.UserDTO;
import com.example.Modeme.User.UserService.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SignupController {

    private final UserService userService;

    /** ======================== [ 회원가입 ] ======================== **/

    // 회원가입 폼
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());

        // 🔥 로그인 상태 확인 후 모델에 추가 (null 방지)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
        model.addAttribute("loggedIn", isLoggedIn); // 항상 true/false 값을 가지도록 설정

        return "/Sign/signup"; // 회원가입 HTML 경로
    }

    // 아이디 중복 확인 API
    @GetMapping("/api/check-username")
    @ResponseBody
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.isUsernameTaken(username));
    }

    // 이메일 중복 확인 API
    @GetMapping("/api/check-email")
    @ResponseBody
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.isEmailTaken(email));
    }

    // 회원가입 처리
    @PostMapping("/signup")
    public String signupProcess(@Valid @ModelAttribute("userDTO") UserDTO userDTO, 
                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/Sign/signup"; // 유효성 검사 실패 시 다시 회원가입 폼으로 이동
        }

        // 아이디 중복 확인
        if (userService.isUsernameTaken(userDTO.getUsername())) {
            model.addAttribute("usernameError", "이미 사용 중인 아이디입니다.");
            return "/Sign/signup";
        }

        // 이메일 중복 확인
        if (userService.isEmailTaken(userDTO.getEmail())) {
            model.addAttribute("emailError", "이미 사용 중인 이메일입니다.");
            return "/Sign/signup";
        }

        try {
            userService.registerUser(userDTO);
            return "redirect:/signup?success=true"; // 🔥 회원가입 성공 시 로그인 페이지로 이동
        } catch (Exception e) {
            model.addAttribute("errorMessage", "회원가입 중 오류가 발생했습니다. 다시 시도해주세요.");
            return "/Sign/signup";
        }
    }
}
