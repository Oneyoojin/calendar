package com.example.calendar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@RequestMapping("/api/sample")
public class SampleController {

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
    public String login(@RequestParam String email, @RequestParam String password) {
        // 로그인 처리 (예: 사용자 인증)
        // 로그인 성공 시 /home 페이지로 리디렉션
        return "redirect:/home";
    }
}