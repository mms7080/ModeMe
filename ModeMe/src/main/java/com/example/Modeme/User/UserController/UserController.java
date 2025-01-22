package com.example.Modeme.User.UserController;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.Modeme.User.UserDTO.Headerlogin;
import com.example.Modeme.User.UserDTO.UserDTO;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserService.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final Headerlogin keep; // 로그인 유지 재사용 Headerlogin 클래스
    private final UserService userService;

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

    // 회원정보 수정 페이지 (현재 로그인한 사용자 정보 불러오기)
    @GetMapping("/modify")
    public String modifyForm(Model model, Principal principal) {
        String username = principal.getName(); // 현재 로그인한 사용자 ID 가져오기
        User user = userService.findByUsername(username); // DB에서 사용자 정보 조회

        // 조회한 사용자 정보를 UserDTO로 변환하여 Model에 추가
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setPhone(user.getPhone());
        userDTO.setBirthdate(user.getBirthdate());
        userDTO.setGender(user.getGender());
        userDTO.setPostcode(user.getPostcode());
        userDTO.setAddress(user.getAddress());
        userDTO.setAddressDetail(user.getAddressDetail());

        model.addAttribute("userDTO", userDTO);
        return "/Sign/modify"; // 회원정보 수정 페이지
    }

    @PostMapping("/modify")
    public String modifyProcess(@Valid @ModelAttribute("userDTO") UserDTO userDTO, 
                                BindingResult bindingResult, 
                                Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            return "/Sign/modify"; // 유효성 검사 실패 시 다시 폼으로 이동
        }

        String username = principal.getName(); // 현재 로그인한 사용자 ID 가져오기
        userService.updateUser(username, userDTO); // 회원정보 업데이트

        // DB에서 갱신된 사용자 정보를 다시 불러와 확인
        User updatedUser = userService.findByUsername(username);
        System.out.println("회원정보 수정 완료: " + updatedUser.getEmail() + ", " + updatedUser.getPhone());

        model.addAttribute("successMessage", "회원정보가 성공적으로 수정되었습니다.");
        return "redirect:/modify?success"; // 수정 완료 후 다시 회원정보 수정 페이지로 이동
    }

}
