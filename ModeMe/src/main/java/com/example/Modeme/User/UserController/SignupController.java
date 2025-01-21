package com.example.Modeme.User.UserController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public String signupProcess(@Valid UserDTO userDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/Sign/signup"; // 유효성 오류 발생 시 다시 폼으로 이동
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
            return "redirect:/signup?success";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "회원가입 중 오류가 발생했습니다. 다시 시도해주세요.");
            return "/Sign/signup";
        }
    }
}
