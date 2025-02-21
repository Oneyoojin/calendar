package com.example.service;

import com.example.calendar.dto.Userdto;
import com.example.calendar.entity.Users;
import com.example.calendar.entity.Users.Gender;
import com.example.calendar.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        // 아이디 유효성 검사
        if (userDto.getUsername() == null || userDto.getUsername().length() > 9) {
            return "아이디는 최대 8자까지 입력 가능합니다.";
        }
        if (!Pattern.matches("^[a-zA-Z0-9]+$", userDto.getUsername())) {
            return "아이디는 영문과 숫자로만 입력해야 합니다.";
        }

        // 비밀번호 유효성 검사
        if (userDto.getPassword() == null || userDto.getPassword().length() > 8) {
            return "비밀번호는 최대 8자까지 입력 가능합니다.";
        }
        if (!Pattern.matches("^[a-zA-Z0-9]+$", userDto.getPassword())) {
            return "비밀번호는 영문자와 숫자만 포함해야 합니다.";
        }

        // 이미 존재하는 아이디 및 이메일 확인
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

        // 생년월일 유효성 검사
        LocalDate birthdate = userDto.getBirthdate();
        if (birthdate == null) {
            return "생년월일을 입력해주세요.";
        }

        // 성별 유효성 검사
        Gender gender;
        try {
            gender = Gender.valueOf(userDto.getGender().toUpperCase());
        } catch (IllegalArgumentException e) {
            return "올바르지 않은 성별 값입니다. (남성, 여성, 기타 중 선택)";
        }

        // 사용자 객체 생성 및 저장
        Users user = Users.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))  // 비밀번호 암호화
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

    // 사용자 존재 여부 확인
    public boolean isExistByUsername(String username) {
        return usersRepository.findByUsername(username).isPresent();
    }

    // 비밀번호 재설정 처리 (서비스에서 비밀번호 변경)
    @Transactional
public boolean updatePassword(String username, String newPassword) {
    // 비밀번호 유효성 검사
    if (!isValidPassword(newPassword)) {
        return false;  // 비밀번호가 유효하지 않으면 false 반환
    }

    // 사용자 조회
    Optional<Users> userOptional = usersRepository.findByUsername(username);
    if (userOptional.isPresent()) {
        Users user = userOptional.get();

        // 비밀번호만 업데이트 (이메일은 null로 설정되지 않도록 처리)
        user.setPassword(passwordEncoder.encode(newPassword));

        // 이메일이 null 또는 빈 문자열인 경우 기본값 설정
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            user.setEmail("default@example.com");  // 기본 이메일을 설정
        }

        // 사용자 정보 저장
        usersRepository.save(user);
        return true;  // 비밀번호 업데이트 성공
    }

    return false;  // 사용자 정보가 없으면 false 반환
    }

    // 비밀번호 유효성 검사
    private boolean isValidPassword(String password) {
        // 비밀번호 조건: 최소 6자, 최대 8자, 영문자+숫자 포함
        String passwordRegex = "^[a-zA-Z0-9]{6,8}$";
        return Pattern.matches(passwordRegex, password);
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

    // 비밀번호 재설정 처리 (새로운 비밀번호로 업데이트)
    public String resetPassword(String username, String newPassword) {
        Optional<Users> userOptional = usersRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            user.setPassword(passwordEncoder.encode(newPassword));  // 새로운 비밀번호로 업데이트
            usersRepository.save(user);
            return "비밀번호가 재설정되었습니다. 새 비밀번호로 로그인하세요.";
        } else {
            return "아이디를 찾을 수 없습니다.";
        }
    }
}
