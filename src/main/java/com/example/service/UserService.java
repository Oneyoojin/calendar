package com.example.service;

import com.example.calendar.dto.Userdto;
import com.example.calendar.entity.Users;
import com.example.calendar.entity.Users.Gender;
import com.example.calendar.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public String registerUser(Userdto userDto) {
        // 1️⃣ 아이디 중복 검사
        if (usersRepository.findByUsername(userDto.getUsername()).isPresent()) {
            return "이미 가입된 아이디입니다.";
        }

        // 2️⃣ 생년월일 변환
        LocalDate birthdate;
        try {
            birthdate = LocalDate.parse(userDto.getBirthdate());
        } catch (DateTimeParseException e) {
            return "생년월일 형식이 올바르지 않습니다. (예: 2000-01-01)";
        }

        // 3️⃣ 성별 변환
        Gender gender;
        try {
            gender = Gender.valueOf(userDto.getGender().toUpperCase());
        } catch (IllegalArgumentException e) {
            return "올바르지 않은 성별 값입니다. (남성, 여성, 기타 중 선택)";
        }

        // 4️⃣ 사용자 저장 (빌더 패턴 활용)
        Users user = Users.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .dateOfBirth(birthdate)
                .gender(gender)
                .isDomestic("대한민국".equals(userDto.getNationality()))
                .build();

        usersRepository.save(user);
        return "회원가입 성공!";
    }
}
