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
            .securityMatcher("/api/sample/**")  // ✅ 보안 적용 URL을 /api/sample/** 경로로 한정
            .csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/sample/register", "/api/sample/find-username2").permitAll()
                .requestMatchers("/api/sample/find-username3", "/api/sample/process-find-username").permitAll()  // ✅ 추가된 허용 경로
                .requestMatchers("/api/sample/login").permitAll()  
                .requestMatchers("/error").permitAll()  // ✅ 에러 페이지 접근 허용
                .requestMatchers("/api/sample/all").permitAll()
                .requestMatchers("/api/sample/member").authenticated()
                .requestMatchers("/api/sample/admin").hasRole("ADMIN")
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()  // ✅ 정적 리소스 허용
                .requestMatchers("/success.html").permitAll() // ✅ success.html 페이지 접근 허용
                .requestMatchers("/api/sample/success").permitAll() // ✅ /api/sample/success 경로 허용
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/api/sample/login")  
                .loginProcessingUrl("/login")  
                .defaultSuccessUrl("/api/sample/member", true)
                .failureUrl("/api/sample/login?error=true")  
                .permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/api/sample/login")  
                .defaultSuccessUrl("/api/sample/member", true)  
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")  
                .logoutSuccessUrl("/api/sample/login?logout=true")  
                .invalidateHttpSession(true)  
                .clearAuthentication(true)  
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
        // InMemoryUserDetailsManager를 이용하여 간단한 사용자 관리 (실제 DB로 변경 가능)
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
    
    // 추가된 부분: 정적 자원 접근을 허용하는 부분
    @Bean
    public SecurityFilterChain staticResourcesSecurity(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests()
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()  // 정적 리소스 허용
                .anyRequest().authenticated();
        return http.build();
    }
}
