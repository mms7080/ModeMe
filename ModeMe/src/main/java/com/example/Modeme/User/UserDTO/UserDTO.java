package com.example.Modeme.User.UserDTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserDTO {

    @NotBlank(message = "아이디는 필수 입력 사항입니다.")
    @Size(min = 4, max = 16, message = "아이디는 4자 이상 16자 이하로 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 사항입니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    private String name;

    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    private String email;

    private String phone;

    // 생일 필드를 LocalDate로 변경
    private LocalDate birthdate;

    private String gender;

    private String postcode;

    private String address;

    private String addressDetail;
    
}
