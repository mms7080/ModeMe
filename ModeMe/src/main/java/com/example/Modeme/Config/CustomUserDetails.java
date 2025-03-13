package com.example.Modeme.Config;

import com.example.Modeme.User.UserEntity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomUserDetails implements UserDetails, OAuth2User { // ✅ OAuth2User 추가

    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes; // ✅ OAuth2User 정보 저장

    // 일반 로그인용 생성자
    public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user != null ? user : new User(); // ✅ User가 없을 경우 기본 생성
        this.authorities = authorities != null ? new ArrayList<>(authorities) : new ArrayList<>();
    }

    // OAuth2 로그인용 생성자
    public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
        this.user = user != null ? user : new User();
        this.authorities = authorities != null ? new ArrayList<>(authorities) : new ArrayList<>();
        this.attributes = attributes;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword() != null ? user.getPassword() : "";
    }

    @Override
    public String getUsername() {
        return user.getUsername() != null ? user.getUsername() : "";
    }

    public String getRole() {
        return user.getRole() != null ? user.getRole() : "USER";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // ✅ OAuth2User 구현 (반드시 추가해야 오류 해결됨)
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return user.getUsername(); // OAuth2User의 기본 식별자 반환
    }
}
