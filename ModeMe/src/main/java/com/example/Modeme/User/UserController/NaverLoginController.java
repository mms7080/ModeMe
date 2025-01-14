package com.example.Modeme.User.UserController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/naver_callback")
public class NaverLoginController {

    @GetMapping
    public String handleNaverCallback(@RequestParam String code, @RequestParam String state) {
        // 네이버 API를 통해 accessToken 및 사용자 정보를 가져옵니다.
        String accessToken = getAccessToken(code, state);
        Map<String, Object> userInfo = getUserInfo(accessToken);

        if (userInfo != null && userInfo.containsKey("name")) {
            String username = (String) userInfo.get("name");
            return "네이버 로그인 성공! 환영합니다, " + username + "님!";
        }

        return "네이버 로그인 실패! 사용자 정보를 가져올 수 없습니다.";
    }

    private String getAccessToken(String code, String state) {
        // 네이버 API를 호출하여 AccessToken을 가져오는 로직
        // 예: REST API 호출
        return "mockAccessToken"; // 실제 구현 필요
    }

    private Map<String, Object> getUserInfo(String accessToken) {
        // AccessToken을 사용하여 사용자 정보를 가져오는 로직
        // 예: REST API 호출
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", "홍길동"); // 실제 구현 필요
        return userInfo;
    }
}
