package com.example.Modeme.User.UserDTO;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String confirmPassword;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private LocalDate birthdate;

    private String gender;

    private String postcode;
    private String address;
    private String addressDetail;

    // 전화번호를 세 개의 input으로 받음
    private String phone1;
    private String phone2;
    private String phone3;

    // 실제 저장할 통합된 phone 필드
    private String phone;
}

