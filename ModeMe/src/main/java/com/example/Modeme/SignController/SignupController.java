package com.example.Modeme.SignController;
//메롱
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}