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
            .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화
            .authorizeRequests(auth -> auth
                .requestMatchers(
                    "/api/calendar/register", 
                    "/api/calendar/find-username", 
                    "/api/calendar/find-username-result",  // 리다이렉트 허용
                    "/api/calendar/find-username2", 
                    "/api/calendar/reset-password",  
                    "/api/calendar/login", 
                    "/api/calendar/process-find-username",
                    "/error", 
                    "/api/calendar/all",
                    "/api/calendar/success"
                ).permitAll()  // 로그인 없이 접근 가능한 페이지 설정
                
                .requestMatchers("/api/calendar/member").authenticated()  // 회원만 접근 가능
                .requestMatchers("/api/calendar/admin").hasRole("ADMIN")  // 관리자만 접근 가능
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()  // 정적 리소스 허용
                
                .anyRequest().authenticated()  // 나머지 요청은 인증 필요
            )
            .formLogin(login -> login
                .loginPage("/api/calendar/login")  // 로그인 페이지 설정
                .loginProcessingUrl("/login")  
                .defaultSuccessUrl("/api/calendar/all", true)  // 로그인 성공 후 리디렉션
                .failureUrl("/api/calendar/login?error=true")  // 로그인 실패 후 리디렉션
                .permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/api/calendar/login")  // OAuth2 로그인 페이지 설정
                .defaultSuccessUrl("/api/calendar/member", true)  // OAuth2 로그인 성공 후 리디렉션
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")  
                .logoutSuccessUrl("/api/calendar/login?logout=true")  // 로그아웃 후 리디렉션
                .invalidateHttpSession(true)  
                .clearAuthentication(true)  
                .permitAll()
            )
            .sessionManagement()
                .invalidSessionUrl("/api/calendar/login"); // 세션 만료 시 로그인 페이지로 리디렉션

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // 비밀번호 암호화 방식
    }

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
