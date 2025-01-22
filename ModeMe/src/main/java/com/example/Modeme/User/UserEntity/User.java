package com.example.Modeme.User.UserEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 ID
    private Long id;

    @Column(nullable = false, unique = true, length = 20) // 아이디는 유니크, 필수
    private String username;

    @Column(nullable = false) // 비밀번호는 필수
    private String password;

    @Column(nullable = false, length = 50) // 이름 필수
    private String name;

    @Column(nullable = false, unique = true) // 이메일은 유니크, 필수
    private String email;

    @Column(length = 13) // 전화번호는 선택
    private String phone;

    @Column // 생년월일은 선택
    private LocalDate birthdate;

    @Column(length = 6) // 성별은 선택 (남성/여성)
    private String gender;

    @Column(length = 10) // 우편번호
    private String postcode;

    @Column(length = 255) // 기본 주소
    private String address;

    @Column(length = 255) // 상세 주소
    private String addressDetail;

    @Column(length = 10, nullable = false) // 권한 필드 추가, 기본값은 user
    private String role = "user"; // 기본값 설정

    @CreationTimestamp // 회원가입 시 자동으로 현재 날짜 및 시간이 입력됨
    @Column(nullable = false, updatable = false) // 수정 불가능하도록 설정
    private LocalDateTime createdAt;
}
