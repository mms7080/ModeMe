package com.example.Modeme.QnA.QnA;

import java.time.LocalDateTime;

import com.example.Modeme.User.UserEntity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Qna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    
    private User user;
    
    @Column(name = "is_secret")
    private boolean secret;
    
    @Column(name = "secret_password", nullable = true)
    private String secretPassword; // 비밀글 확인용 비밀번호

    private LocalDateTime createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isSecret() {
	    return secret;
	}

	public void setSecret(boolean secret) {
	    this.secret = secret;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	public String getSecretPassword() {
	    return secretPassword;
	}

	public void setSecretPassword(String secretPassword) {
	    this.secretPassword = secretPassword;
	}
    
}

