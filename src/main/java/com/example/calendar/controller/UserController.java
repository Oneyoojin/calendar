package com.example.calendar.controller;

import com.example.calendar.dto.Userdto;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // 로그인 페이지로 이동
    }

    @GetMapping("/all") // 로그인 후 첫 메인화면
    public String showAllPage() {
        return "all";  // all.html 템플릿을 반환
    }

    @GetMapping("/find-username") // 아이디 찾기 페이지
    public String findUsernamePage() {
        return "find-username";  // find-username.html로 이동
    }

    @PostMapping("/find-username")
    public String findUsername(@RequestParam String email, @RequestParam String birthdate, Model model) throws UnsupportedEncodingException {
        LocalDate birthDate = LocalDate.parse(birthdate); // 문자열을 LocalDate로 변환
        String foundUsername = userService.findUsernameByEmailAndBirthdate(email, birthDate);

        if (foundUsername == null || foundUsername.isEmpty()) {
            String errorMessage = "일치하는 아이디를 찾을 수 없습니다.";
            String encodedErrorMessage = URLEncoder.encode(errorMessage, "UTF-8");
            return "redirect:/api/calendar/success?error=" + encodedErrorMessage;
        }

        String encodedUsername = URLEncoder.encode(foundUsername, "UTF-8");
        return "redirect:/api/calendar/success?username=" + encodedUsername;
    }

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "terms-agreement";  // 회원가입 약관 동의 페이지
    }

    @PostMapping("/register")
    public String redirectToFindUsername2() {
        return "redirect:/api/calendar/find-username2";  // 아이디 찾기 페이지로 리다이렉트
    }

    @GetMapping("/find-username2")
    public String findUsername2Page() {
        return "find-username2";  // 아이디 찾기 페이지
    }

    @PostMapping("/process-find-username2")
    public String processFindUsername2(@RequestParam String username, Model model) {
        // 아이디를 find-username-result.html로 전달
        model.addAttribute("username", username); // 아이디 전달
        return "find-username-result"; // 아이디 찾기 결과 페이지로 리디렉션
    }

    @GetMapping("/success")
    public String showSuccessPage(@RequestParam(name = "username", required = false) String username,
                                  @RequestParam(name = "error", required = false) String error,
                                  Model model) throws UnsupportedEncodingException {
        if (username != null) {
            String decodedUsername = URLDecoder.decode(username, "UTF-8");
            model.addAttribute("username", decodedUsername);
            model.addAttribute("message", decodedUsername + "님, 지금부터 기능을 사용할 수 있습니다.");
        } else if (error != null) {
            String decodedError = URLDecoder.decode(error, "UTF-8");
            model.addAttribute("error", decodedError);
        } else {
            model.addAttribute("username", "사용자 이름 없음");
        }
        return "success";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordPage() {
        return "reset-password";
    }

    @PostMapping("/process-reset-password")
    public String processResetPassword(@RequestParam String username, Model model) {
        String result = userService.findUserByUsername(username);

        if (result == null) {
            model.addAttribute("error", "아이디를 찾을 수 없습니다.");
            return "reset-password";
        }

        return "redirect:/api/calendar/reset-password-success";
    }

    @GetMapping("/reset-password-success")
    public String showResetPasswordSuccessPage() {
        return "reset-password-success";
    }
}
