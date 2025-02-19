package com.example.Modeme.User.UserEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 13)
    private String phone;

    @Column
    private LocalDate birthdate;

    @Column(length = 6)
    private String gender;

    @Column(length = 10)
    private String postcode;

    @Column(length = 255)
    private String address;

    @Column(length = 255)
    private String addressDetail;

    @Column(length = 10, nullable = false)
    private String role = "user";

    // ✅ 회원 가입 날짜 자동 저장
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
