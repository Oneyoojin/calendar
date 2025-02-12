package com.example.calendar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // SecurityFilterChain을 설정하여 요청에 대한 접근을 제어
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests()
                .requestMatchers("/api/sample/all").permitAll()
                .requestMatchers("/api/sample/member").authenticated()
                .requestMatchers("/api/sample/admin").hasRole("ADMIN")
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/api/sample/login")
                .loginProcessingUrl("/login")  // 로그인 처리 URL 설정
                .defaultSuccessUrl("/home", true)
                .permitAll()
            .and()
            .logout()
                .permitAll();
        return http.build();
    }

    // AuthenticationManager 빈을 설정
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService);  // UserDetailsService 설정
        return authenticationManagerBuilder.build();  // AuthenticationManager 반환
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
        
        // 암호화된 비밀번호를 사용하여 사용자 등록
        manager.createUser(User.withUsername("user")
                            .password(passwordEncoder.encode("password"))  // 비밀번호 암호화
                            .roles("USER")
                            .build());
        manager.createUser(User.withUsername("user1")
                            .password(passwordEncoder.encode("1111"))  // 비밀번호 "1111"을 암호화
                            .roles("USER")
                            .build());
        manager.createUser(User.withUsername("admin")
                            .password(passwordEncoder.encode("admin"))
                            .roles("ADMIN")
                            .build());
        return manager;
    }
}