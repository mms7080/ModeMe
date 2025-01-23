package com.example.Modeme.User.UserController;

import com.example.Modeme.User.UserService.NaverAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/auth/naver")
@RequiredArgsConstructor
public class NaverAuthController {

    private final NaverAuthService naverAuthService;

    @GetMapping("/callback")
    public void naverCallback(@RequestParam String code, @RequestParam String state, HttpServletResponse response) throws IOException {
        String token = naverAuthService.getAccessToken(code, state);
        String email = naverAuthService.getUserInfo(token);

        System.out.println("✅ 네이버 로그인 성공, 이메일: " + email);

        // ✅ 팝업 창을 닫고 부모 창을 업데이트
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("<script>window.opener.location.href='/mainpage'; window.close();</script>");
    }
}
