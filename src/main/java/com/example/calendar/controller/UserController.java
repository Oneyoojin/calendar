package com.example.calendar.controller;

import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDate;

@Log4j2
@Controller
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 로그인 페이지
    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 틀렸습니다.");
        }
        return "login";  // 로그인 페이지로 이동
    }

    // 로그인 요청 처리 (성공 시 member 페이지로 이동)
    @PostMapping("/login")
    public String processLogin() {
        return "redirect:/api/calendar/member";  // 로그인 성공 후 member 페이지로 이동
    }

    // 로그인 후 첫 메인화면
    @GetMapping("/all")
    public String showAllPage() {
        return "all";  // all.html 템플릿을 반환
    }

    // 회원 페이지 (로그인 성공 후 이동)
    @GetMapping("/member")
    public String showMemberPage() {
        return "member";  // member 페이지 반환
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
            model.addAttribute("error", "일치하는 아이디를 찾을 수 없습니다."); 
            return "find-username"; 
        }

        model.addAttribute("username", foundUsername);
        return "find-username";
    }

    // 회원가입 약관 동의 페이지
    @GetMapping("/register")
    public String showRegistrationPage() {
        return "terms-agreement";  
    }

    // 회원가입 약관 동의 후 아이디 찾기 페이지로 리다이렉트
    @PostMapping("/register")
    public String redirectToFindUsername2() {
        return "redirect:/api/calendar/find-username2";  
    }

    // 아이디 찾기 페이지2
    @GetMapping("/find-username2")
    public String findUsername2Page() {
        return "find-username2";  
    }

    // 아이디 찾기 결과 처리
    @PostMapping("/process-find-username2")
    public String processFindUsername2(@RequestParam String username, Model model) {
        model.addAttribute("username", username);
        return "find-username-result"; 
    }

    // 성공 페이지 처리
    @GetMapping("/success")
    public String showSuccessPage(@RequestParam(name = "username", required = false) String username,
                                  @RequestParam(name = "error", required = false) String error,
                                  Model model) throws UnsupportedEncodingException {
        if (username != null) {
            model.addAttribute("username", URLDecoder.decode(username, "UTF-8"));
            model.addAttribute("message", username + "님, 지금부터 기능을 사용할 수 있습니다.");
        } else if (error != null) {
            model.addAttribute("error", URLDecoder.decode(error, "UTF-8"));
        } else {
            model.addAttribute("username", "사용자 이름 없음");
        }
        return "success";
    }

    // 비밀번호 찾기 페이지 (GET 요청)
    @GetMapping("/resetPasswordPage")
    public String showResetPasswordPage(@RequestParam(required = false) String username, Model model) {
        if (username != null) {
            String result = userService.findUserByUsername(username);
            if (result == null || result.isEmpty()) {
                model.addAttribute("error", "아이디를 찾을 수 없습니다.");
                return "resetPasswordPage";  
            } else {
                model.addAttribute("username", username);
                return "redirect:/api/calendar/resetPasswordPage-success?username=" + username;
            }
        }
        return "resetPasswordPage";  
    }

    // 비밀번호 재설정 처리 (POST 요청)
    @PostMapping("/resetPasswordPage")
    public String processResetPasswordPage(@RequestParam String username, Model model) {
        if (!userService.isExistByUsername(username)) {
            model.addAttribute("error", "아이디를 찾을 수 없습니다.");
            return "resetPasswordPage";  
        }
        model.addAttribute("username", username);
        return "redirect:/api/calendar/resetPasswordPage-success?username=" + username;  
    }

    // 비밀번호 재설정 처리 (GET 요청)
    @GetMapping("/resetPasswordPage-success")
    public String showResetPasswordSuccessPage(@RequestParam String username, Model model) {
        if (username == null || username.isEmpty()) {
            return "redirect:/api/calendar/login";  
        }
        model.addAttribute("username", username);
        return "reset-password-success";  
    }

    // 비밀번호 재설정 후 POST 요청 처리
    @PostMapping("/resetPasswordPage-success")
    public String handleResetPasswordSuccess(@RequestParam String username,
                                             @RequestParam String newPassword,
                                             @RequestParam String confirmPassword,
                                             Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "비밀번호 확인이 일치하지 않습니다.");
            model.addAttribute("username", username);
            return "reset-password-success";  
        }

        if (!userService.isExistByUsername(username)) {
            model.addAttribute("error", "없는 아이디입니다.");
            return "reset-password-success";  
        }

        boolean isUpdated = userService.updatePassword(username, newPassword);
        if (isUpdated) {
            model.addAttribute("message", "비밀번호가 재설정되었습니다. 새 비밀번호로 로그인하세요.");
            return "redirect:/api/calendar/reset-pw-success";  
        } else {
            model.addAttribute("error", "아이디 또는 이메일을 확인하세요.");
            return "reset-password-success";  
        }
    }

    // 비밀번호 변경 성공 페이지
    @GetMapping("/reset-pw-success")
    public String showResetPasswordSuccess() {
        return "reset-pw-success";  
    }

    // reset-password 페이지로 이동
    @GetMapping("/reset-password")
    public String showResetPasswordPage() {
        return "resetPasswordPage";  
    }

    // 비밀번호 변경 처리 (username을 사용하여 비밀번호 변경)
    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam String username, 
                                  @RequestParam String newPassword, 
                                  Model model) {
        boolean isUpdated = userService.updatePassword(username, newPassword);
        
        if (isUpdated) {
            model.addAttribute("message", "비밀번호가 성공적으로 재설정되었습니다.");
            return "redirect:/login?resetSuccess";  
        } else {
            model.addAttribute("error", "아이디를 확인하거나 비밀번호 변경에 실패했습니다.");
            return "redirect:/reset-password?error";  
        }
    }
}
