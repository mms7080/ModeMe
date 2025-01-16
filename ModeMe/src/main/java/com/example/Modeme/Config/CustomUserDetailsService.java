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
         .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

     return new CustomUserDetails(user);
 }

 }



