package com.example.calendar.controller;

import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // 비밀번호 찾기 페이지 (GET 요청)
    @GetMapping("/resetPasswordPage")  // GET 요청 처리
    public String showResetPasswordPage(@RequestParam(required = false) String username, Model model) {
        if (username != null) {
            // 사용자 아이디가 존재하는지 확인
            String result = userService.findUserByUsername(username);

            if (result == null || result.isEmpty()) {
                // 아이디가 없으면 에러 메시지 전달
                model.addAttribute("error", "아이디를 찾을 수 없습니다.");
                return "resetPasswordPage";  // 오류 시 리디렉션 없이 같은 뷰를 반환
            } else {
                // 아이디가 존재하면 그 아이디를 모델에 추가
                model.addAttribute("username", username);
                // 아이디가 존재하면 비밀번호 재설정 성공 페이지로 리디렉트
                return "redirect:/api/calendar/resetPasswordPage-success?username=" + username;
            }
        }
        return "resetPasswordPage";  // 정상적인 경우 뷰 반환
    }

    // 비밀번호 재설정 처리 (POST 요청)
    @PostMapping("/resetPasswordPage")  // POST 요청 처리
    public String processResetPasswordPage(@RequestParam String username, Model model) {
        // 아이디 존재 여부 체크: 아이디가 없다면 error 메시지 출력
        if (!userService.isExistByUsername(username)) {
            model.addAttribute("error", "아이디를 찾을 수 없습니다.");
            return "resetPasswordPage";  // 아이디가 없으면 다시 같은 페이지로
        }

        // 아이디가 존재한다면 비밀번호 재설정 성공 페이지로 리디렉션
        model.addAttribute("username", username);
        return "redirect:/api/calendar/resetPasswordPage-success?username=" + username;  
    }

    // 비밀번호 재설정 처리 (GET 요청)
    @GetMapping("/resetPasswordPage-success")
    public String showResetPasswordSuccessPage(@RequestParam String username, Model model) {
        if (username == null || username.isEmpty()) {
            return "redirect:/api/calendar/login";  // username이 없으면 로그인 페이지로 리디렉션
        }
        model.addAttribute("username", username);
        return "reset-password-success";  // 비밀번호 재설정 성공 페이지 반환
    }

    // 비밀번호 재설정 후 POST 요청 처리
    @PostMapping("/resetPasswordPage-success")
    public String handleResetPasswordSuccess(@RequestParam String username,
                                             @RequestParam String newPassword,
                                             @RequestParam String confirmPassword,
                                             Model model) {

        // 새 비밀번호와 확인 비밀번호가 일치하는지 확인
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "비밀번호 확인이 일치하지 않습니다.");
            model.addAttribute("username", username);
            return "reset-password-success";  // 비밀번호 확인이 일치하지 않으면 다시 페이지로 돌아감
        }

        // 사용자 존재 여부 확인
        if (!userService.isExistByUsername(username)) {
            model.addAttribute("error", "없는 아이디입니다.");
            return "reset-password-success";  // 아이디가 존재하지 않으면 오류 메시지 표시
        }

        // 비밀번호 업데이트 서비스 호출
        boolean isUpdated = userService.updatePassword(username, newPassword);

        if (isUpdated) {
            model.addAttribute("message", "비밀번호가 재설정되었습니다. 새 비밀번호로 로그인하세요.");
            return "redirect:/api/calendar/reset-pw-success"; // 비밀번호 재설정 후 성공 페이지로 리디렉션
        } else {
            model.addAttribute("error", "아이디 또는 이메일을 확인하세요.");
            return "reset-password-success";  // 업데이트 실패 시 오류 메시지 표시
        }
    }

    // 비밀번호 변경 성공 페이지
    @GetMapping("/reset-pw-success")
    public String showResetPasswordSuccess() {
        return "reset-pw-success";  // 비밀번호 변경 성공 페이지로 이동
    }

    // reset-password 페이지로 이동
    @GetMapping("/reset-password")
    public String showResetPasswordPage() {
        return "resetPasswordPage";  // resetPasswordPage.html로 이동
    }

    // 비밀번호 변경 처리 (username을 사용하여 비밀번호 변경)
    @PostMapping("/reset-password")
    public String handleResetPassword(@RequestParam String username, 
                                  @RequestParam String newPassword, 
                                  Model model) {
        boolean isUpdated = userService.updatePassword(username, newPassword);
        
        if (isUpdated) {
            model.addAttribute("message", "비밀번호가 성공적으로 재설정되었습니다.");
            return "redirect:/login?resetSuccess";  // 비밀번호 재설정 성공 시 로그인 페이지로 리디렉션
        } else {
            model.addAttribute("error", "아이디를 확인하거나 비밀번호 변경에 실패했습니다.");
            return "redirect:/reset-password?error";  // 실패 시 에러 페이지로 리디렉션
        }
    }
}
