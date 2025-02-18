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
        // 기존 회원가입 로직
        if (userDto.getUsername() == null || userDto.getUsername().length() > 8) {
            return "아이디는 최대 8자까지 입력 가능합니다.";
        }
        if (!Pattern.matches("^[a-zA-Z0-9]+$", userDto.getUsername())) {
            return "아이디는 영문과 숫자로만 입력해야 합니다.";
        }

        if (userDto.getPassword() == null || userDto.getPassword().length() > 8) {
            return "비밀번호는 최대 8자까지 입력 가능합니다.";
        }
        if (!Pattern.matches("^[a-zA-Z0-9]+$", userDto.getPassword())) {
            return "비밀번호는 영문과 숫자로만 입력해야 합니다.";
        }

        Optional<Users> existingUser = usersRepository.findByUsername(userDto.getUsername());
        if (existingUser.isPresent()) {
            return "이미 가입된 아이디입니다.";
        }

        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            return "이메일을 입력해주세요.";
        }

        Optional<Users> existingEmail = usersRepository.findByEmail(userDto.getEmail());
        if (existingEmail.isPresent()) {
            return "이미 가입된 이메일입니다.";
        }

        LocalDate birthdate = userDto.getBirthdate();
        if (birthdate == null) {
            return "생년월일을 입력해주세요.";
        }

        Gender gender;
        try {
            gender = Gender.valueOf(userDto.getGender().toUpperCase());
        } catch (IllegalArgumentException e) {
            return "올바르지 않은 성별 값입니다. (남성, 여성, 기타 중 선택)";
        }

        Users user = Users.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .dateOfBirth(birthdate)
                .gender(gender)
                .isDomestic(userDto.isDomestic())
                .isActive(true)
                .build();

        usersRepository.save(user);
        return "회원가입 성공!";
    }

    // 아이디 찾기 기능 추가
    public String findUsernameByEmailAndBirthdate(String email, LocalDate birthdate) {
        // 이메일과 생년월일을 사용해 사용자 조회
        Optional<Users> userOptional = usersRepository.findByEmailAndDateOfBirth(email, birthdate);

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            return "찾은 아이디: " + user.getUsername(); // 아이디 반환
        } else {
            return "일치하는 아이디가 없습니다."; // 일치하는 사용자가 없을 경우
        }
    }
}
