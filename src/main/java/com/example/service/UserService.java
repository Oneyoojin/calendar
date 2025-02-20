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

    // 회원가입 로직
    public String registerUser(Userdto userDto) {
        if (userDto.getUsername() == null || userDto.getUsername().length() > 9) {
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

    // 아이디 찾기
    public String findUsernameByEmailAndBirthdate(String email, LocalDate birthdate) {
        Optional<Users> userOptional = usersRepository.findByEmailAndDateOfBirth(email, birthdate);

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            return "찾은 아이디: " + user.getUsername();
        } else {
            return "일치하는 아이디가 없습니다.";
        }
    }

    // 아이디 존재 여부 확인
    public String findUserByUsername(String username) {
        Optional<Users> userOptional = usersRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            return "아이디가 확인되었습니다. 비밀번호 재설정 페이지로 이동합니다.";
        } else {
            return "아이디를 찾을 수 없습니다.";
        }
    }
    public boolean isExistByUsername(String username) {
        return usersRepository.findByUsername(username).isPresent();
    }

    // 비밀번호 재설정 처리
    public String resetPassword(String username) {
        Optional<Users> userOptional = usersRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            String newPassword = "newRandomPassword";  // 임시 비밀번호 (랜덤 생성 필요)
            user.setPassword(passwordEncoder.encode(newPassword));
            usersRepository.save(user);
            return "비밀번호가 재설정되었습니다. 새 비밀번호로 로그인하세요.";
        } else {
            return "아이디를 찾을 수 없습니다.";
        }
    }

    // 사용자 아이디 중복 확인
    public boolean checkUserExistsByUsername(String username) {
        return usersRepository.findByUsername(username).isPresent();
    }

    // 사용자 이메일 중복 확인
    public boolean checkUserExistsByEmail(String email) {
        return usersRepository.findByEmail(email).isPresent();
    }

    // 사용자 아이디와 이메일을 통해 사용자 조회
    public Optional<Users> findByUsernameAndEmail(String username, String email) {
        return usersRepository.findByUsernameAndEmail(username, email);
    }

    // 비밀번호 업데이트
    public boolean updatePassword(String username, String email, String newPassword) {
        // 사용자 조회
        Optional<Users> userOptional = usersRepository.findByUsernameAndEmail(username, email);

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            // 비밀번호 암호화 후 업데이트
            user.setPassword(passwordEncoder.encode(newPassword));
            usersRepository.save(user);  // 비밀번호 저장
            return true;
        }

        return false;  // 사용자 정보가 없으면 실패
    }
}
