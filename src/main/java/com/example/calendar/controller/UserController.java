package com.example.calendar.controller;

import com.example.service.UserService;
import lombok.RequiredArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 로그인 페이지
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // 로그인 페이지로 이동
    }

    // 로그인 후 첫 메인화면
    @GetMapping("/all")
    public String showAllPage() {
        return "all";  // all.html 템플릿을 반환
    }

    // 아이디 찾기 페이지
    @GetMapping("/find-username")
    public String findUsernamePage() {
        return "find-username";  // find-username.html로 이동
    }

    // 아이디 찾기 처리
    @PostMapping("/find-username")
    public String findUsername(@RequestParam String email, @RequestParam String birthdate, Model model) {
        LocalDate birthDate = LocalDate.parse(birthdate); // 문자열을 LocalDate로 변환
        String foundUsername = userService.findUsernameByEmailAndBirthdate(email, birthDate);

        if (foundUsername == null || foundUsername.isEmpty()) {
            model.addAttribute("error", "일치하는 아이디를 찾을 수 없습니다."); // 에러 메시지를 모델에 추가
            return "find-username";  // 같은 페이지로 돌아가면서 오류 메시지 표시
        }

        model.addAttribute("username", foundUsername);  // 아이디가 발견되었을 경우 해당 아이디를 모델에 추가
        return "find-username";  // 찾은 아이디를 페이지에 표시
    }

    // 회원가입 약관 동의 페이지
    @GetMapping("/register")
    public String showRegistrationPage() {
        return "terms-agreement";  // 회원가입 약관 동의 페이지
    }

    // 회원가입 약관 동의 후 아이디 찾기 페이지로 리다이렉트
    @PostMapping("/register")
    public String redirectToFindUsername2() {
        return "redirect:/api/calendar/find-username2";  // 아이디 찾기 페이지로 리다이렉트
    }

    // 아이디 찾기 페이지2
    @GetMapping("/find-username2")
    public String findUsername2Page() {
        return "find-username2";  // 아이디 찾기 페이지
    }

    // 아이디 찾기 결과 처리
    @PostMapping("/process-find-username2")
    public String processFindUsername2(@RequestParam String username, Model model) {
        model.addAttribute("username", username); // 아이디 전달
        return "find-username-result"; // 아이디 찾기 결과 페이지로 리디렉션
    }

    // 성공 페이지 처리
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

    // 비밀번호 찾기 페이지
    @GetMapping("/reset-password")
    public String showResetPasswordPage() {
        return "reset-password";  // 비밀번호 찾기 페이지 반환
    }

    // 비밀번호 찾기 처리
    @PostMapping("/process-reset-password")
    public String processResetPassword(@RequestParam String username, Model model) {
        // 사용자 아이디로 사용자 존재 여부 확인
        String result = userService.findUserByUsername(username);

        // 사용자가 존재하지 않으면 에러 메시지 처리
        if (result == null) {
            model.addAttribute("error", "아이디를 찾을 수 없습니다.");  // 오류 메시지를 모델에 추가
            return "reset-password";  // 비밀번호 찾기 페이지로 다시 돌아가기
        }

        // 비밀번호 재설정 성공 처리 (비밀번호 재설정 절차에 따라 수정 가능)
        return "redirect:/api/calendar/reset-password-success?username=" + username;  // 비밀번호 재설정 성공 페이지로 이동
    }

    // 비밀번호 재설정 처리 (GET 요청)
    @GetMapping("/reset-password-success")
    public String showResetPasswordSuccessPage(@RequestParam String username, Model model) {
        model.addAttribute("username", username);
        return "reset-password-success";  // 비밀번호 재설정 성공 페이지 반환
    }

    // 비밀번호 재설정 후 POST 요청 처리
    @PostMapping("/reset-password-success")
    public String handleResetPasswordSuccess(@RequestParam String username, @RequestParam String email, Model model) {
        // 비밀번호 재설정 서비스 호출
        boolean isUpdated = userService.updatePassword(username, email, "newRandomPassword123");

        if (isUpdated) {
            model.addAttribute("message", "비밀번호가 재설정되었습니다. 새 비밀번호로 로그인하세요.");
        } else {
            model.addAttribute("error", "아이디 또는 이메일을 확인하세요.");
        }

        return "redirect:/api/calendar/find-username-result"; // 결과 페이지로 리디렉션
    }
}
