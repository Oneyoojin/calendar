package com.example.calendar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/sample")
public class UserController {

    // ✅ 로그인 페이지 (Spring Security 설정과 일치하도록 URL 수정)
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // login.html 반환 (templates/login.html이 있어야 함)
    }

    // ✅ 회원가입 약관 동의 페이지
    @GetMapping("/register")
    public String showRegistrationPage() {
        return "terms-agreement";  // terms-agreement.html 페이지 반환
    }

    // ✅ 회원가입 처리 후 find-username2로 리디렉션 (POST 요청)
    @PostMapping("/register")
    public String processRegistration() {
        return "redirect:/api/sample/find-username2";  // 회원가입 후 아이디 찾기 페이지로 리디렉션
    }

    // ✅ 아이디 찾기 페이지
    @GetMapping("/find-username2")
    public String findUsername2Page() {
        return "find-username2";  // find-username2.html 페이지 반환
    }
}
