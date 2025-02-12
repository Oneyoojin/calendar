package com.example.calendar.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@RequestMapping("/api/sample")
public class SampleController {

    private final AuthenticationManager authenticationManager;

    public SampleController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    // /api/sample/all 경로를 처리
    @GetMapping("/all")
    public String exAll() {
        return "all";  // all.html 페이지로 리턴
    }

    // /api/sample/member 경로를 처리
    @GetMapping("/member")
    public String exMember() {
        return "member";  // member.html 페이지로 리턴
    }

    // /api/sample/admin 경로를 처리
    @GetMapping("/admin")
    public String exAdmin() {
        return "admin";  // admin.html 페이지로 리턴
    }

    // 로그인 페이지를 처리
    @GetMapping("/login")
    public String showLoginPage() {
        log.info("/api/sample/login 요청 처리");
        return "login";  // login.html 페이지로 리턴
    }

    // 로그인 처리 (POST 요청)
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        try {
            // AuthenticationManager를 사용해 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
            // 인증 성공 시 SecurityContext에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 로그인 성공 시 /home 페이지로 리디렉션
            return "redirect:/home";
        } catch (Exception e) {
            log.error("Login failed for user: " + username, e);
            model.addAttribute("error", "Invalid username or password.");
            return "login";  // login.html 페이지로 리턴
        }
    }
}