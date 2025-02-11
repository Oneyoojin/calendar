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
                .requestMatchers("/api/sample/all").permitAll()  // requestMatchers 사용
                .requestMatchers("/api/sample/member").authenticated()
                .requestMatchers("/api/sample/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/api/sample/login")
                .permitAll()
            .and()
            .logout()
                .permitAll();

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user")
                                .password("{noop}password")
                                .roles("USER")
                                .build());
        manager.createUser(User.withUsername("admin")
                                .password("{noop}admin")
                                .roles("ADMIN")
                                .build());
        return manager;
    }
}