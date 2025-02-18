package com.example.service;

import com.example.calendar.dto.Userdto;
import com.example.calendar.entity.Users;
import com.example.calendar.entity.Users.Gender;
import com.example.calendar.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public String registerUser(Userdto userDto) {
        // 1️⃣ 아이디 유효성 검사 (최대 8자, 영문+숫자만 허용)
        if (userDto.getUsername() == null || userDto.getUsername().length() > 8) {
            return "아이디는 최대 8자까지 입력 가능합니다.";
        }
        if (!Pattern.matches("^[a-zA-Z0-9]+$", userDto.getUsername())) {
            return "아이디는 영문과 숫자로만 입력해야 합니다.";
        }

        // 2️⃣ 비밀번호 유효성 검사 (최대 8자, 영문+숫자만 허용)
        if (userDto.getPassword() == null || userDto.getPassword().length() > 8) {
            return "비밀번호는 최대 8자까지 입력 가능합니다.";
        }
        if (!Pattern.matches("^[a-zA-Z0-9]+$", userDto.getPassword())) {
            return "비밀번호는 영문과 숫자로만 입력해야 합니다.";
        }

        // 3️⃣ 아이디 중복 검사
        Optional<Users> existingUser = usersRepository.findByUsername(userDto.getUsername());
        if (existingUser.isPresent()) {
            return "이미 가입된 아이디입니다.";
        }

        // 4️⃣ 이메일 중복 검사 (이메일이 입력된 경우에만)
        if (userDto.getEmail() != null && !userDto.getEmail().isEmpty()) {
            Optional<Users> existingEmail = usersRepository.findByEmail(userDto.getEmail());
            if (existingEmail.isPresent()) {
                return "이미 가입된 이메일입니다.";
            }
        }

        // 5️⃣ 생년월일 검증
        LocalDate birthdate = userDto.getBirthdate();
        if (birthdate == null) {
            return "생년월일을 입력해주세요.";
        }

        // 6️⃣ 성별 변환 (String → Enum)
        Gender gender;
        try {
            gender = Gender.valueOf(userDto.getGender().toUpperCase());
        } catch (IllegalArgumentException e) {
            return "올바르지 않은 성별 값입니다. (남성, 여성, 기타 중 선택)";
        }

        // 7️⃣ 사용자 저장
        Users user = Users.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword())) // 비밀번호 암호화
                .email(userDto.getEmail() != null && !userDto.getEmail().isEmpty() ? userDto.getEmail() : null) // 이메일이 비어 있으면 null로 설정
                .dateOfBirth(birthdate)
                .gender(gender)
                .isDomestic(userDto.isDomestic())
                .isActive(true) // 기본값 설정
                .build();

        // 8️⃣ 사용자 저장
        usersRepository.save(user);
        return "회원가입 성공!";
    }
}
