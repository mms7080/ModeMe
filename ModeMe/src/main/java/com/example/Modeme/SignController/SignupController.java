package com.example.Modeme.SignController;
//메롱
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class SignupController {
   //회원가입
   @GetMapping("/signup") 
   public String signup() {
      return "/Sign/Signup";
   }
   //로그인
   @GetMapping("/signin") 
   public String signin() {
      return "/Sign/Signin";
   }
   //아이디찾기
   @GetMapping("/find_id")
   public String find_id() {
	   return "/Sign/find_id";
   }
   //비밀번호찾기
   @GetMapping("/find_pw")
   public String find_pw() {
	   return "/Sign/find_pw";
   }
   //회원정보수정
   @GetMapping("/modify")
   public String modify() {
	   return "/Sign/modify";
   }
   
   @RestController
   @RequestMapping("/naver_callback")
   public class NaverLoginController {

       @GetMapping
       public String handleNaverCallback(@RequestParam String code, @RequestParam String state) {
           // 여기서 네이버 API를 호출하여 accessToken 및 사용자 정보를 가져옵니다.
           // 예시: AccessToken 발급 및 사용자 정보 가져오기

           // 로그인 후 원하는 페이지로 리디렉션
           return "redirect:/main"; // "/main" 페이지로 리디렉션
       }
   }

}