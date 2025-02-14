package com.example.calendar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // 🔥 CSRF 보호 비활성화 (테스트용)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/sample/register", "/api/sample/find-username2").permitAll()  // 회원가입 및 find-username2 허용
                .requestMatchers("/find-username2.html").permitAll()  // 정적 HTML 파일 접근 허용
                .requestMatchers("/api/sample/all").permitAll()  // 공개된 API
                .requestMatchers("/api/sample/member").authenticated()  // 인증된 사용자만 접근
                .requestMatchers("/api/sample/admin").hasRole("ADMIN")  // ADMIN 역할만 접근
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()  // 정적 리소스 공개
                .anyRequest().authenticated()  // 나머지 요청은 인증 필요
            )
            .formLogin(login -> login
                .loginPage("/api/sample/login")  // 로그인 페이지
                .loginProcessingUrl("/login")  
                .defaultSuccessUrl("/api/sample/member", true)  // 로그인 성공 후 리디렉션
                .permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/api/sample/login")  // OAuth2 로그인 페이지
                .defaultSuccessUrl("/api/sample/member", true)  // 로그인 성공 후 리디렉션
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")  // 로그아웃 URL
                .logoutSuccessUrl("/api/sample/login?logout=true")  // 로그아웃 후 리디렉션 URL
                .invalidateHttpSession(true)  // 세션 무효화
                .clearAuthentication(true)  // 인증 정보 클리어
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // InMemory 방식으로 사용자 생성 (실제 DB로 변경 가능)
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user1")
                            .password(passwordEncoder.encode("1111"))
                            .roles("USER")
                            .build());
        manager.createUser(User.withUsername("admin")
                            .password(passwordEncoder.encode("admin123"))
                            .roles("ADMIN")
                            .build());
        return manager;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
            .userDetailsService(userDetailsService(passwordEncoder))
            .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }
}
