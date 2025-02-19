package com.example.Modeme.User.UserDTO;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    public interface Create {} // 회원가입용 유효성 검사 그룹
    public interface Update {} // 회원정보 수정용 유효성 검사 그룹

    @NotBlank(message = "아이디는 필수 입력 항목입니다.", groups = Create.class)
    @Pattern(regexp = "^[a-z0-9]{4,16}$", message = "아이디는 영문 소문자와 숫자로 4~16자이어야 합니다.", groups = Create.class)
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.", groups = Create.class)
    @Size(min = 10, message = "비밀번호는 최소 10자 이상이어야 합니다.", groups = Create.class)
    private String password; // ✅ 회원가입 시 필수, 수정 시 선택 가능

    private String confirmPassword;

    @Pattern(regexp = "\\d{10,11}", message = "전화번호는 숫자로만 이루어진 10~11자리여야 합니다.", groups = Create.class)
    private String phone; // ✅ 숫자로만 구성된 10~11자리 전화번호 입력 검증

    @NotBlank(message = "이름이 작성되지 않았습니다.", groups = Create.class)
    private String name;

    private String gender;
    private String postcode;
    private String address;
    private String addressDetail;

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email; // ✅ 입력된 경우에만 이메일 형식 검증

    private LocalDate birthdate;
}