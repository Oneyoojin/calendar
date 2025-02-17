package com.example.calendar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/sample")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "terms-agreement";
    }

    @PostMapping("/register")
    public String redirectToFindUsername2() {
        return "redirect:/api/sample/find-username2";
    }

    @GetMapping("/find-username2")
    public String findUsername2Page() {
        return "find-username2";
    }
}
