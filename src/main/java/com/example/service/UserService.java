package com.example.service;

import com.example.calendar.dto.Userdto;
import com.example.calendar.entity.Users;
import  com.example.calendar.entity.Users.Gender;
import com.example.calendar.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 처리 (중복 아이디 검사)
     */
    public String registerUser(Userdto userDto) {
        // 1️⃣ 아이디 중복 검사
        if (usersRepository.findByUsername(userDto.getUsername()).isPresent()) {
            return "이미 가입된 아이디입니다.";
        }

        // 2️⃣ 생년월일 유효성 검사 및 변환
        LocalDate birthdate;
        try {
            birthdate = LocalDate.parse(userDto.getBirthdate()); // "YYYY-MM-DD" 형식의 문자열을 LocalDate로 변환
        } catch (DateTimeParseException e) {
            return "생년월일 형식이 올바르지 않습니다. (예: 2000-01-01)";
        }

        // 3️⃣ 성별 유효성 검사 및 변환
        com.example.calendar.entity.Users.Gender gender;
        try {
            gender = Gender.valueOf(userDto.getGender()); // 문자열을 Enum으로 변환
        } catch (IllegalArgumentException e) {
            return "올바르지 않은 성별 값입니다. (남성, 여성, 기타 중 선택)";
        }

        // 4️⃣ 새 사용자 저장
        Users user = new Users();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // 비밀번호 암호화
        user.setEmail(userDto.getEmail());
        user.setDateOfBirth(birthdate); // LocalDate 저장
        user.setGender(gender); // Enum 저장
        user.setIsDomestic("대한민국".equals(userDto.getNationality()));

        usersRepository.save(user);  // DB 저장
        return "회원가입 성공!";
    }
}
