package com.example.Modeme.Manager.ManagerDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.Modeme.User.UserEntity.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDataDTO {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String role;
    private String createdAt;
    private Long qnaCount;
    private Long reviewCount;

    // 기본 생성자
    public UserDataDTO() {}

    // 사용자 정의 생성자
    public UserDataDTO(User user, Long qnaCount, Long reviewCount) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        // 회원 유형을 '일반' 또는 '관리자'로 변환
        this.role = user.getRole().equals("admin") ? "관리자" : "일반";
        
        // 가입일을 'yyyy/MM/dd' 형태로 변환
        this.createdAt = user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        this.qnaCount = qnaCount;
        this.reviewCount = reviewCount;
    }

    // Getters
    public Long getId() {
        return id;
    }
    
    public String getName() {
    	return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Long getQnaCount() {
        return qnaCount;
    }

    public Long getReviewCount() {
        return reviewCount;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setName(String name) {
    	this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setQnaCount(Long qnaCount) {
        this.qnaCount = qnaCount;
    }

    public void setReviewCount(Long reviewCount) {
        this.reviewCount = reviewCount;
    }
}
