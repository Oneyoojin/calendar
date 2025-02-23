package com.example.calendar;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordCheckExample {
    public static void main(String[] args) {
        // 암호화된 비밀번호
        String encryptedPassword = "$2a$10$K2Wlkj8pJ2aqdraFfSJ3.en5kmFWVz783OPB1Mvvttzzv5plqvGpa";
        
        // 사용자가 입력한 비밀번호 (예: "user1Password")
        String inputPassword = "user12"; 
            
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // 비밀번호가 일치하는지 확인
        boolean isPasswordMatch = passwordEncoder.matches(inputPassword, encryptedPassword);

        if (isPasswordMatch) {
            System.out.println("비밀번호가 일치합니다.");
        } else {
            System.out.println("비밀번호가 일치하지 않습니다.");
        }
    }
}

