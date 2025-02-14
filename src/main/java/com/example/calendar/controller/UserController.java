package com.example.calendar.controller;

import com.example.calendar.entity.Users;
import com.example.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/sample")
public class UserController {

    private final UserService userService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // /api/sample/all 경로를 처리
    @GetMapping("/all")
    public String exAll() {
        return "all";  // all.html 페이지로 리턴
    }

    // 로그인 페이지를 처리
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // login.html 페이지로 리턴
    }

    // 회원가입 페이지로 이동
    @GetMapping("/register")
    public String showRegistrationPage() {
        return "terms-agreement";  // terms-agreement.html 페이지로 이동
    }

    // 회원가입 처리 후 find-username2 페이지로 이동 (POST 요청)
    @PostMapping("/register")
    public String registerAndRedirect() {
        return "redirect:/api/sample/find-username2";  // redirect 수정
    }

    // 구글 로그인 후 리디렉션될 페이지 처리
    @GetMapping("/member")
    public String memberPage(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof OAuth2User oAuth2User) {
            String username = oAuth2User.getName();
            String email = oAuth2User.getAttribute("email");
            model.addAttribute("username", username);
            model.addAttribute("email", email);
        } else {
            model.addAttribute("error", "인증되지 않은 사용자 접근.");
        }

        return "member";  // member.html 페이지로 이동
    }

    // /api/sample/find-username2 경로를 처리
    @GetMapping("/find-username2")
    public String findUsername2Page() {
        return "find-username2";  // templates/find-username2.html로 이동
    }

    // find-username2 폼 제출 처리
    @PostMapping("/find-username2")
    public String handleFormSubmission(
            @RequestParam String birthdate,
            @RequestParam String name,
            @RequestParam String nationality,
            @RequestParam String gender,
            @RequestParam(required = false) String email,
            RedirectAttributes redirectAttributes,
            Model model) {

        // 필수 필드 검증
        if (name == null || name.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "이름을 입력해주세요.");
            return "redirect:/errorPage";
        }

        Users user = new Users();
        user.setUsername(name.trim());

        // 날짜 변환 예외 처리
        try {
            user.setDateOfBirth(LocalDate.parse(birthdate.trim(), DATE_FORMATTER));
        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("error", "생년월일 형식이 올바르지 않습니다. (예: yyyy-MM-dd)");
            return "redirect:/errorPage";
        }

        // 이메일 설정
        if (email != null && !email.trim().isEmpty()) {
            user.setEmail(email.trim());
        }

        // Gender 변환 예외 처리
        try {
            user.setGender(Users.Gender.valueOf(gender.trim().toUpperCase()));
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "성별 값이 올바르지 않습니다. (예: 남성, 여성, 기타)");
            return "redirect:/errorPage";
        }

        // 국적 정보 변환
        user.setIsDomestic("내국인".equals(nationality.trim()));
        user.setIsActive(true);

        // 데이터 저장
        userService.saveUser(user);

        // 모델에 user 객체 추가 (Thymeleaf에서 사용하기 위함)
        model.addAttribute("user", user);

        return "find-username2";  // find-username2.html 페이지로 리턴
    }
}
