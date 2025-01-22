package com.example.Modeme.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ CORS 설정 추가
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    "/signin", 
                    "/api/signin",
                    "/signup", 
                    "/find_id",
                    "/find_pw",
                    "/modify",
                    "/logout",
                    "/qna/**",
                    "/notices/**",
                    "/image/**",
                    "/css/**",
                    "/js/**",
                    "/resources/**",
                    "/address/**",
                    "/address_default",
                    "/address_delete",
                    "/default_delete",
                    "/wishlist_delete",
                    "/manager/**",
                    "/api/**",
                    "/productDetail/**"
                )
            )
            .authorizeHttpRequests(auth -> auth
                // QnA 관련 권한 설정
                .requestMatchers("/qna", "/qna/**").permitAll() // QnA 목록, 상세보기 모두 허용
                .requestMatchers("/qna/write", "/qna/edit/**", "/qna/delete/**").authenticated() // QnA 쓰기/수정/삭제는 인증 필요
                
                // 공지 관련 권한 설정
                .requestMatchers("/notices","/notices/**").permitAll() // 공지 목록 누구나 접근 가능
                .requestMatchers("/notices/new", "/notices/edit/**", "/notices/delete/**","/manager/**").hasRole("ADMIN") // 관리자만 허용

                // 로그인, 회원가입, 정적 리소스는 모두 접근 가능
                .requestMatchers(
                    "/signin",
                    "/signup",
                    "/find_id",
                    "/find_pw",
                    "/modify",
                    "/image/**",
                    "/css/**",
                    "/js/**",
                    "/resources/**",
                    "/wishlist_delete",
                    "/",
                    "/main",
                    "check-username",
                    "check-email"
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

    // ✅ CORS 설정 추가
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // 허용할 도메인 설정
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드
        configuration.setAllowedHeaders(List.of("*")); // 모든 헤더 허용
        configuration.setAllowCredentials(true); // 인증 정보 포함 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 CORS 적용
        return source;
    }
}
