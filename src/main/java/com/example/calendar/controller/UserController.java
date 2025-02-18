package com.example.calendar.controller;

import com.example.calendar.dto.Userdto;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @PostMapping("/find-username") // 아이디 찾기 처리
    public String findUsername(@RequestParam String email, @RequestParam String birthdate, Model model) {
        LocalDate birthDate = LocalDate.parse(birthdate); // 문자열을 LocalDate로 변환
        String foundUsername = userService.findUsernameByEmailAndBirthdate(email, birthDate);

        if (foundUsername == null || foundUsername.isEmpty()) {
            model.addAttribute("error", "일치하는 아이디를 찾을 수 없습니다."); // 실패 메시지 추가
            return "find-username";  // 실패 시 다시 find-username 페이지로 이동
        }

        model.addAttribute("foundUsername", foundUsername); // 찾은 아이디를 모델에 추가
        return "find-username-result"; // 아이디 찾기 성공 후 결과 페이지로 이동
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

    // ✅ 회원가입 처리 (find-username2.html에서 입력된 데이터를 처리)
    @PostMapping("/process-find-username")
    public String processFindUsername(@ModelAttribute Userdto userDto, Model model) {
        String result = userService.registerUser(userDto); // UserService의 회원가입 로직 실행

        if (result.equals("회원가입 성공!")) {
            return "redirect:/api/calendar/success?username=" + userDto.getUsername();
        } else {
            model.addAttribute("error", result); // 실패 시 에러 메시지를 모델에 추가
            return "find-username2"; // 다시 아이디 찾기 페이지로 이동하여 에러 표시
        }
    }

    // ✅ 회원가입 성공 페이지
    @GetMapping("/success")
    public String showSuccessPage(@RequestParam(name = "username", required = false) String username, Model model) {
        model.addAttribute("username", username != null ? username : "사용자 이름 없음");
        return "success";  // success.html 템플릿을 렌더링 (Thymeleaf 템플릿 사용)
    }

    // 비밀번호 찾기 페이지
    @GetMapping("/reset-password")
    public String showResetPasswordPage() {
        return "reset-password";  // reset-password.html 페이지 반환
    }

    // 비밀번호 찾기 처리
    @PostMapping("/process-reset-password")
    public String processResetPassword(@RequestParam String username, Model model) {
        // 사용자 아이디로 DB 확인
        boolean userExists = userService.checkUserExistsByUsername(username);

        if (!userExists) {
            model.addAttribute("error", "아이디를 찾을 수 없습니다.");
            return "reset-password"; // 아이디가 없으면 다시 비밀번호 찾기 페이지로 돌아감
        }

        // 아이디가 존재하면 비밀번호 재설정 페이지로 이동
        return "redirect:/api/calendar/reset-password-success"; // 비밀번호 재설정 성공 페이지로 이동
    }

    // 비밀번호 재설정 성공 페이지
    @GetMapping("/reset-password-success")
    public String showResetPasswordSuccessPage() {
        return "reset-password-success";  // 비밀번호 재설정 성공 페이지 반환
    }
}
