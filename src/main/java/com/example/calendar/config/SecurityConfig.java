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

    // HttpSecurity 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests()
                .requestMatchers("/api/sample/all").permitAll()  // 공개된 API
                .requestMatchers("/api/sample/member").authenticated()  // 인증된 사용자만 접근
                .requestMatchers("/api/sample/admin").hasRole("ADMIN")  // ADMIN 역할만 접근
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()  // 정적 리소스 공개
                .anyRequest().authenticated()  // 나머지 요청은 인증 필요
            .and()
            .formLogin()
                .loginPage("/api/sample/login")  // 커스터마이즈된 로그인 페이지
                .loginProcessingUrl("/login")  // 로그인 처리 URL
                .defaultSuccessUrl("/api/sample/member", true)  // 로그인 성공 후 이동할 URL
                .permitAll()
            .and()
            .oauth2Login()  // Google OAuth2 로그인 활성화
                .loginPage("/api/sample/login")  // 구글 로그인 페이지도 로그인 페이지에 통합
                .defaultSuccessUrl("/api/sample/member", true)  // 구글 로그인 후 /api/sample/member로 리디렉션
                .permitAll()
            .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/api/sample/login?logout=true")  // 로그아웃 후 리디렉션 URL
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll();

        return http.build();
    }

    // 비밀번호 인코더 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // UserDetailsService 설정 (InMemory 방식)
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user1")
                            .password(passwordEncoder.encode("1111"))
                            .roles("USER")
                            .build());
        manager.createUser(User.withUsername("admin")
                            .password(passwordEncoder.encode("admin123"))
                            .roles("ADMIN")
                            .build());  // ADMIN 계정 추가
        return manager;
    }

    // AuthenticationManager 설정
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
            .userDetailsService(userDetailsService(passwordEncoder()))
            .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}