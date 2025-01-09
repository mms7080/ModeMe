package com.example.Modeme.SignController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignupController {
   //회원가입
   @GetMapping("/signup") 
   public String noticehome() {
      return "/Signup/Signup";
   }
}