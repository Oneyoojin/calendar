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

    // 비밀번호 찾기 기능 추가
    public String findUserByUsername(String username) {
        // 사용자 아이디로 DB에서 사용자 조회
        Optional<Users> userOptional = usersRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            // 사용자가 존재하면 해당 아이디에 대한 처리를 반환 (이 예제에서는 바로 성공 메시지를 반환)
            return "아이디가 확인되었습니다. 비밀번호 재설정 페이지로 이동합니다.";
        } else {
            return "아이디를 찾을 수 없습니다."; // 아이디가 없으면 실패 메시지
        }
    }

    // 비밀번호 재설정 처리 (예시로 임시 비밀번호 생성 및 이메일로 발송 등 추가 가능)
    public String resetPassword(String username) {
        // 여기서 비밀번호 재설정 로직을 추가할 수 있습니다.
        // 예를 들어, 임시 비밀번호를 생성하여 이메일로 보내는 등의 작업을 추가할 수 있습니다.
        
        Optional<Users> userOptional = usersRepository.findByUsername(username);
        
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            String newPassword = "newRandomPassword"; // 임시 비밀번호 (랜덤 생성 로직 필요)
            user.setPassword(passwordEncoder.encode(newPassword)); // 비밀번호 변경
            usersRepository.save(user);
            return "비밀번호가 재설정되었습니다. 새 비밀번호로 로그인하세요.";
        } else {
            return "아이디를 찾을 수 없습니다."; // 아이디가 없는 경우
        }
    }

    // 사용자 아이디 존재 확인 (중복체크용)
    public boolean checkUserExistsByUsername(String username) {
        return usersRepository.findByUsername(username).isPresent();
    }
    public boolean checkUserExistsByEmail(String email) {
        // 이메일로 사용자 검색
        return usersRepository.findByEmail(email).isPresent();  // 이메일이 존재하면 true 반환
    }
    
}
