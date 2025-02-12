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

    // SecurityFilterChain을 설정하여 요청에 대한 접근을 제어
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // CSRF 보호 비활성화 (개발 중에만)
            .authorizeHttpRequests()
                .requestMatchers("/api/sample/all").permitAll()  // 공개된 API
                .requestMatchers("/api/sample/member").authenticated()  // 인증된 사용자만 접근
                .requestMatchers("/api/sample/admin").hasRole("ADMIN")  // ADMIN 역할만 접근
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()  // 정적 리소스 공개
                .anyRequest().authenticated()  // 나머지 요청은 인증 필요
            .and()
            .formLogin()
                .loginPage("/api/sample/login")  // 로그인 페이지 설정
                .loginProcessingUrl("/login")  // 로그인 처리 URL
                .defaultSuccessUrl("/home", true)  // 로그인 성공 후 이동할 URL
                .permitAll()  // 로그인 페이지 접근은 누구나 가능
            .and()
            .logout()
                .permitAll();  // 로그아웃은 누구나 가능
        return http.build();
    }

    // 비밀번호 인코더 (BCryptPasswordEncoder 사용)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // BCryptPasswordEncoder 사용
    }

    // UserDetailsService를 사용하여 사용자를 메모리에 등록
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        
        // user1, 1111 설정 추가
        manager.createUser(User.withUsername("user1")
                            .password(passwordEncoder.encode("1111"))  // 비밀번호 암호화
                            .roles("USER")
                            .build());
        manager.createUser(User.withUsername("admin")
                            .password(passwordEncoder.encode("admin"))
                            .roles("ADMIN")
                            .build());
        manager.createUser(User.withUsername("user")
                            .password(passwordEncoder.encode("password"))  // 기존 user
                            .roles("USER")
                            .build());
        return manager;
    }

    // AuthenticationManager 빈 등록
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                   .userDetailsService(userDetailsService(passwordEncoder()))  // 사용자 서비스 설정
                   .passwordEncoder(passwordEncoder())  // 비밀번호 인코더 설정
                   .and()
                   .build();
    }
}