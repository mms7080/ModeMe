package com.example.Modeme.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    "/api/signin", 
                    "/api/signup", 
                    "/logout",
                    "/qna/**",
                    "/notices/**",
                    "/image/**",
                    "/css/**",
                    "/js/**",
                    "/resources/**"
                )
            )
            .authorizeHttpRequests(auth -> auth
                // QnA 관련 권한 설정
                .requestMatchers("/qna", "/qna/**").permitAll() // QnA 목록, 상세보기 모두 허용
                .requestMatchers("/qna/write", "/qna/edit/**", "/qna/delete/**").authenticated() // QnA 쓰기/수정/삭제는 인증 필요
                
                // 공지 관련 권한 설정
                .requestMatchers("/notices","/notices/**").permitAll() // 공지 목록 누구나 접근 가능
                .requestMatchers("/notices/new", "/notices/edit/**", "/notices/delete/**").hasRole("ADMIN") // 관리자만 허용

                // 로그인, 회원가입, 정적 리소스는 모두 접근 가능
                .requestMatchers(
                    "/signin",
                    "/signup",
                    "/image/**",
                    "/css/**",
                    "/js/**",
                    "/resources/**",
                    "/",
                    "/main"
                ).permitAll()

                // 그 외의 모든 요청은 인증 필요
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/signin") // 로그인 페이지 경로 설정
                .loginProcessingUrl("/api/signin") // 로그인 처리 경로 설정
                .defaultSuccessUrl("/", true) // 로그인 성공 시 이동 경로 설정
                .failureUrl("/signin?error=true") // 로그인 실패 시 이동 경로 설정
                .usernameParameter("username") // 로그인 폼의 아이디 필드 이름
                .passwordParameter("password") // 로그인 폼의 비밀번호 필드 이름
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // 로그아웃 처리 경로
                .logoutSuccessUrl("/signin") // 로그아웃 성공 후 이동 경로
                .invalidateHttpSession(true) // 세션 무효화
                .deleteCookies("JSESSIONID") // 쿠키 삭제
            )
            .rememberMe(rm -> rm
                .key("SecureRandomKeyForEncryption") // Remember-Me 토큰 암호화 키
                .tokenValiditySeconds(3600 * 24 * 7) // Remember-Me 토큰 유효기간 (7일)
                .userDetailsService(customUserDetailsService) // 사용자 인증정보를 불러올 서비스
            );

        return http.build();
    }
}
