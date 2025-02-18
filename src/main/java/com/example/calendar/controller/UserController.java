package com.example.calendar.controller;

import com.example.calendar.dto.Userdto;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
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
            // ✅ 회원가입 성공 시 success 페이지로 이동하면서 username을 전달
            return "redirect:/api/calendar/success?username=" + userDto.getUsername();
        } else {
            model.addAttribute("error", result); // 실패 시 에러 메시지를 모델에 추가
            return "find-username2"; // 다시 아이디 찾기 페이지로 이동하여 에러 표시
        }
    }

    // ✅ 회원가입 성공 페이지
    @GetMapping("/success")
    public String showSuccessPage(@RequestParam(name = "username", required = false) String username, Model model) {
        // 사용자 이름을 모델에 추가하여 success.html로 전달
        model.addAttribute("username", username != null ? username : "사용자 이름 없음");
        return "success";  // success.html 템플릿을 렌더링 (Thymeleaf 템플릿 사용)
    }
}
