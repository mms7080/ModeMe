package com.example.Modeme.Config;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.Modeme.User.UserEntity.User;
import com.example.Modeme.User.UserRepository.UserRepository;

//CustomUserDetailsService: 데이터베이스에서 사용자 정보를 로드하여 UserDetails로 변환
@Service
public class CustomUserDetailsService implements UserDetailsService {

 private final UserRepository userRepository;

 public CustomUserDetailsService(UserRepository userRepository) {
     this.userRepository = userRepository;
 }

 @Override
 public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     User user = userRepository.findByUsername(username)
         .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

     // 디버깅: 데이터베이스의 role 확인
     System.out.println("Database Role: " + user.getRole()); // 데이터베이스에서 가져온 역할

     // GrantedAuthority 생성
     List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));

     // 디버깅: 생성된 권한 목록 확인
     System.out.println("Granted Authorities: " + authorities);

     return new org.springframework.security.core.userdetails.User(
         user.getUsername(),
         user.getPassword(),
         authorities
     );
 }



}
