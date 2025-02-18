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
                .requestMatchers("/api/calendar/register", "/api/calendar/find-username2").permitAll()
                .requestMatchers("/api/calendar/find-username3", "/api/calendar/process-find-username").permitAll()  // 추가된 허용 경로
                .requestMatchers("/api/calendar/login").permitAll()
                .requestMatchers("/error").permitAll()  // 에러 페이지 접근 허용
                .requestMatchers("/api/calendar/all").permitAll()
                .requestMatchers("/api/calendar/member").authenticated()
                .requestMatchers("/api/calendar/admin").hasRole("ADMIN")
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()  // 정적 리소스 허용
                .requestMatchers("/success.html").permitAll()  // success.html 페이지 접근 허용
                .requestMatchers("/api/calendar/success").permitAll()  // /api/calendar/success 경로 허용
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/api/calendar/login")  // 로그인 페이지 수정
                .loginProcessingUrl("/login")  
                .defaultSuccessUrl("/api/calendar/member", true)
                .failureUrl("/api/calendar/login?error=true")  
                .permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/api/calendar/login")  // OAuth2 로그인 페이지 수정
                .defaultSuccessUrl("/api/calendar/member", true)  
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")  
                .logoutSuccessUrl("/api/calendar/login?logout=true")  
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
}
