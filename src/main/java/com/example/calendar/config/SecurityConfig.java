package com.example.calendar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests()
                .requestMatchers("/api/sample/all").permitAll()  // /all 페이지 접근 허용
                .requestMatchers("/api/sample/member").authenticated()  // /member 페이지는 인증 필요
                .requestMatchers("/api/sample/admin").hasRole("ADMIN")  // /admin 페이지는 ADMIN 역할 필요
                .requestMatchers( "/css/**", "/js/**", "/images/**").permitAll()  // 정적 리소스에 대한 접근 허용
                .anyRequest().authenticated()  // 그 외 모든 요청은 인증 필요
            .and()
            .formLogin()
                .loginPage("/api/sample/login")  // 로그인 페이지 경로 설정
                .loginProcessingUrl("/login")  // 로그인 요청 처리 URL 설정 (Spring Security가 자동으로 POST 요청 처리)
                .defaultSuccessUrl("/home", true)  // 로그인 성공 후 리디렉션 URL 설정
                .permitAll()  // 로그인 페이지 접근 허용
            .and()
            .logout()
                .permitAll();  // 로그아웃 허용

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user")
                                .password("{noop}password")  // 비밀번호 암호화 방식을 noop으로 설정
                                .roles("USER")
                                .build());
        manager.createUser(User.withUsername("admin")
                                .password("{noop}admin")
                                .roles("ADMIN")
                                .build());
        return manager;
    }
}