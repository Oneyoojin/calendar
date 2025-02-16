package com.example.service;

import com.example.calendar.dto.Userdto;
import com.example.calendar.entity.Users;
import com.example.calendar.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.calendar.entity.Users.Gender;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 처리 (중복 아이디 검사 및 사용자 저장)
     */
    @Transactional
    public String registerUser(Userdto userDto) {
        // 1️⃣ 아이디 중복 검사
        if (usersRepository.findByUsername(userDto.getUsername()).isPresent()) {
            return "이미 가입된 아이디입니다.";  // 중복된 경우 메시지 반환
        }

        // 2️⃣ 새 사용자 저장
        Users user = new Users();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword())); // 비밀번호 암호화
        user.setEmail(userDto.getEmail());

        // ✅ 생년월일(LocalDate 변환) 적용
        try {
            user.setDateOfBirth(LocalDate.parse(userDto.getBirthdate())); 
        } catch (DateTimeParseException e) {
            return "생년월일 형식이 올바르지 않습니다."; // 날짜 변환 실패 시 메시지 반환
        }

        // ✅ 성별(Gender Enum) 변환 적용
        try {
            user.setGender(Gender.valueOf(userDto.getGender().toUpperCase()));
        } catch (IllegalArgumentException e) {
            return "올바르지 않은 성별 값입니다. (남성, 여성, 기타 중 하나 선택)";
        }

        // ✅ 국적 판단 (대한민국 여부)
        user.setIsDomestic("대한민국".equals(userDto.getNationality()));

        // 3️⃣ DB 저장
        usersRepository.save(user);
        return "회원가입 성공!";
    }
}
