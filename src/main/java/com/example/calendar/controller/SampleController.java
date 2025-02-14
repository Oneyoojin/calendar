package com.example.calendar.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Log4j2
@RequestMapping("/api/sample")
public class SampleController {

    // /api/sample/all 경로를 처리
    @GetMapping("/all")
    public String exAll() {
        log.info("요청: /api/sample/all");
        return "all";  // all.html 페이지로 리턴
    }

    // 로그인 페이지를 처리
    @GetMapping("/login")
    public String showLoginPage() {
        log.info("요청: /api/sample/login");
        return "login";  // login.html 페이지로 리턴
    }

    // 회원가입 페이지로 이동
    @GetMapping("/register")
    public String showRegistrationPage() {
        log.info("요청: /api/sample/register");
        return "terms-agreement";  // terms-agreement.html 페이지로 이동
    }

    // 회원가입 처리 후 find-username2 페이지로 이동 (POST 요청)
    @PostMapping("/register")
    public String registerAndRedirect() {
        log.info("회원가입 완료 → find-username2 페이지로 리디렉션");

        // find-username2.html이 static 폴더에 있을 경우
        return "redirect:/find-username2.html";

        // 만약 find-username2.html이 Thymeleaf(templates 폴더) 안에 있을 경우
        // return "find-username2";
    }

    // 구글 로그인 후 리디렉션될 페이지 처리
    @GetMapping("/member")
    public String memberPage(Model model) {
        log.info("요청: /api/sample/member");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) principal;
            String username = oAuth2User.getName();
            String email = oAuth2User.getAttribute("email");
            log.info("구글 로그인 사용자: {}, 이메일: {}", username, email);

            model.addAttribute("username", username);
            model.addAttribute("email", email);
        } else {
            log.info("인증된 사용자: {}", principal);
        }

        return "member";  
    }

    // 아이디 찾기 페이지를 처리 (GET 요청)
    @GetMapping("/find-username")
    public String findUsernamePage() {
        log.info("요청: /api/sample/find-username");
        return "find-username";  
    }

    // 아이디 찾기 처리 (POST 요청)
    @PostMapping("/find-username")
    public String processFindUsername(@RequestParam String username, 
                                      @RequestParam String birthdate, 
                                      Model model) {
        log.info("아이디 찾기 요청 - 이름: {}, 생년월일: {}", username, birthdate);

        if ("testUser".equals(username) && "2000-01-01".equals(birthdate)) {
            model.addAttribute("foundUsername", "testUser123");
            model.addAttribute("registeredDate", "2025. 1. 1");
            return "find_id_success";  
        } else {
            model.addAttribute("error", "입력한 정보가 일치하지 않습니다.");
            return "find-username";  
        }
    }

    // find-username2 페이지로 이동
    @GetMapping("/find-username2")
    public String findUsername2Page() {
        log.info("요청: /api/sample/find-username2");
        return "find-username2"; // templates/find-username2.html 로 이동
    }
}
