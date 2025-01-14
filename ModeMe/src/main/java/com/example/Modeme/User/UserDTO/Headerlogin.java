package com.example.Modeme.User.UserDTO;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

// 로그인 유지 코드 재사용 위한 클래스
@Component
public class Headerlogin {
	public void headerlogin(Model model, Principal principal) {
		if (principal != null) {
	         // 사용자가 로그인한 경우
	         model.addAttribute("loggedIn", true);
	         model.addAttribute("username", principal.getName());
	      } else {
	         // 사용자가 로그인하지 않은 경우
	         model.addAttribute("loggedIn", false);
	      }
	}
}
