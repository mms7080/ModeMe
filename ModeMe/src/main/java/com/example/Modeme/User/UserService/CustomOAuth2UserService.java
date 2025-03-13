package com.example.Modeme.User.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.Modeme.Config.CustomUserDetails;
import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        System.out.println("🔹 [OAuth2] 전체 응답 데이터: " + attributes);

        // ✅ 어떤 OAuth2 제공자인지 구분 (naver, kakao)
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String email = null;
        String name = null;

        if ("naver".equals(provider)) {
            attributes = (Map<String, Object>) attributes.get("response");
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
            System.out.println("✅ [네이버 로그인] 사용자 이메일: " + email);
            System.out.println("✅ [네이버 로그인] 사용자 이름: " + name);
        } 
        else if ("kakao".equals(provider)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            email = (String) kakaoAccount.get("email");

            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            name = (String) profile.get("nickname");

            System.out.println("✅ [카카오 로그인] 사용자 이메일: " + email);
            System.out.println("✅ [카카오 로그인] 사용자 이름: " + name);
        } 
        else {
            throw new OAuth2AuthenticationException("❌ 지원되지 않는 OAuth2 제공자: " + provider);
        }

        // ✅ 기존 사용자 확인
        Optional<User> existingUser = userRepository.findByUsername(email);

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = new User();
            user.setUsername(email);
            user.setName(name);
            user.setEmail(email);
            user.setRole("user"); // 기본 권한 "user" 설정
            userRepository.save(user);
        }

        // ✅ 권한 설정
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        // ✅ OAuth2User와 호환되는 CustomUserDetails 반환
        return new CustomUserDetails(user, new ArrayList<>(authorities), attributes);
    }
}
