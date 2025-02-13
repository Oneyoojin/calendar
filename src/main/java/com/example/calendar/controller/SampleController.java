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
        return "all";  // all.html 페이지로 리턴
    }

    // 로그인 페이지를 처리
    @GetMapping("/login")
    public String showLoginPage() {
        log.info("/api/sample/login 요청 처리");
        return "login";  // login.html 페이지로 리턴
    }

    // 구글 로그인 후 리디렉션될 페이지 처리
    @GetMapping("/member")
    public String memberPage(Model model) {
        // 인증된 사용자 정보 가져오기
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) principal;
            String username = oAuth2User.getName();
            String email = oAuth2User.getAttribute("email"); // 구글에서 제공하는 사용자 이메일 정보
            log.info("Authenticated Google user: " + username + ", Email: " + email);

            // 모델에 사용자 정보를 전달하여 뷰에서 사용할 수 있도록 함
            model.addAttribute("username", username);
            model.addAttribute("email", email);
        } else {
            log.info("Authenticated user: " + principal);
        }

        return "member";  // member.html 페이지로 리턴
    }

    // 아이디 찾기 페이지를 처리 (GET 요청)
    @GetMapping("/find-username")
    public String findUsernamePage() {
        return "find-username";  // find-username.html 페이지로 리턴
    }

    // 아이디 찾기 처리 (POST 요청)
    @PostMapping("/find-username")
    public String processFindUsername(@RequestParam String username, @RequestParam String birthdate, Model model) {
        log.info("아이디 찾기 요청: 이름={}, 생년월일={}", username, birthdate);

        // 예제: 임시 사용자 데이터 (실제 서비스에서는 DB 조회 필요)
        if ("testUser".equals(username) && "2000-01-01".equals(birthdate)) {
            model.addAttribute("foundUsername", "testUser123");
            model.addAttribute("registeredDate", "2025. 1. 1");
            return "find_id_success";  // find_id_success.html로 이동
        } else {
            model.addAttribute("error", "입력한 정보가 일치하지 않습니다.");
            return "find-username";  // 다시 입력 페이지로 이동
        }
    }
}
