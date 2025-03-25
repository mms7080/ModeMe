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
    @GetMapping("signup")
    public String signupForm(Model model) {
        model.addAttribute("userDTO", new UserDTO());

        // 🔥 로그인 상태 확인 후 모델에 추가 (null 방지)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isLoggedIn = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
        model.addAttribute("loggedIn", isLoggedIn); // 항상 true/false 값을 가지도록 설정

        return "Sign/signup"; // 회원가입 HTML 경로
    }

    // 아이디 중복 확인 API
    @GetMapping("api/check-username")
    @ResponseBody
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        return ResponseEntity.ok(userService.isUsernameTaken(username));
    }

    // 회원가입 처리
    @PostMapping("signup")
    public ResponseEntity<?> signupProcess(@Valid @ModelAttribute("userDTO") UserDTO userDTO, 
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("유효성 검사 실패: " + bindingResult.getAllErrors());
        }

        if (userService.isUsernameTaken(userDTO.getUsername())) {
            return ResponseEntity.badRequest().body("이미 사용 중인 아이디입니다.");
        }

        try {
            userService.registerUser(userDTO);
            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            e.printStackTrace(); // 콘솔에 상세한 오류 로그 출력
            return ResponseEntity.internalServerError().body("회원가입 중 오류 발생: " + e.getMessage());
        }
    }
}
