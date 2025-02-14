package com.example.calendar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/sample")
public class UserController {

    // 회원가입 약관 동의 페이지
    @GetMapping("/register")
    public String showRegistrationPage() {
        return "terms-agreement";  // terms-agreement.html 페이지를 반환
    }

    // 아이디 찾기 페이지
    @GetMapping("/find-username2")
    public String findUsername2Page() {
        return "find-username2";  // find-username2.html 페이지를 반환
    }

    // 로그인을 위한 페이지 (추가)
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // login.html 페이지를 반환
    }
}
