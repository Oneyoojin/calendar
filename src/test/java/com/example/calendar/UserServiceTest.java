package com.example.calendar;

import com.example.calendar.dto.Userdto;
import com.example.calendar.entity.Users;
import com.example.calendar.entity.Users.Gender;
import com.example.calendar.repository.UsersRepository;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService; // 테스트할 서비스

    @Mock
    private UsersRepository usersRepository; // Mock 객체로 대체

    @Mock
    private PasswordEncoder passwordEncoder; // Mock 객체로 대체

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("✅ 정상적인 회원가입 테스트")
    void registerUser_success() {
        // Given: 유저 DTO 생성
        Userdto userDto = new Userdto("testUser", "pass123", "test@example.com",
                LocalDate.of(2000, 1, 1), "남성", true);

        when(usersRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.empty());
        when(usersRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // When: 회원가입 실행
        String result = userService.registerUser(userDto);

        // Then: 성공 메시지 검증
        assertEquals("회원가입 성공!", result);
        verify(usersRepository, times(1)).save(any(Users.class));
    }

    @Test
    @DisplayName("❌ 아이디 중복 체크")
    void registerUser_duplicateUsername() {
        // Given
        Userdto userDto = new Userdto("duplicateUser", "pass123", "test@example.com",
                LocalDate.of(2000, 1, 1), "남성", true);

        when(usersRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.of(new Users()));

        // When
        String result = userService.registerUser(userDto);

        // Then
        assertEquals("이미 가입된 아이디입니다.", result);
        verify(usersRepository, never()).save(any(Users.class));
    }

    @Test
    @DisplayName("❌ 이메일 중복 체크")
    void registerUser_duplicateEmail() {
        // Given
        Userdto userDto = new Userdto("uniqueUser", "pass123", "duplicate@example.com",
                LocalDate.of(2000, 1, 1), "남성", true);

        when(usersRepository.findByUsername(userDto.getUsername())).thenReturn(Optional.empty());
        when(usersRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(new Users()));

        // When
        String result = userService.registerUser(userDto);

        // Then
        assertEquals("이미 가입된 이메일입니다.", result);
        verify(usersRepository, never()).save(any(Users.class));
    }

    @Test
    @DisplayName("❌ 아이디 유효성 검사 실패 (8자 초과)")
    void registerUser_invalidUsername() {
        // Given
        Userdto userDto = new Userdto("TooLongID", "pass123", "test@example.com",
                LocalDate.of(2000, 1, 1), "남성", true);

        // When
        String result = userService.registerUser(userDto);

        // Then
        assertEquals("아이디는 최대 8자까지 입력 가능합니다.", result);
    }

    @Test
    @DisplayName("❌ 비밀번호 유효성 검사 실패 (8자 초과)")
    void registerUser_invalidPassword() {
        // Given
        Userdto userDto = new Userdto("testUser", "TooLongPwd", "test@example.com",
                LocalDate.of(2000, 1, 1), "남성", true);

        // When
        String result = userService.registerUser(userDto);

        // Then
        assertEquals("비밀번호는 최대 8자까지 입력 가능합니다.", result);
    }

    @Test
    @DisplayName("❌ 생년월일이 없을 경우")
    void registerUser_noBirthdate() {
        // Given
        Userdto userDto = new Userdto("testUser", "pass123", "test@example.com",
                null, "남성", true);

        // When
        String result = userService.registerUser(userDto);

        // Then
        assertEquals("생년월일을 입력해주세요.", result);
    }

    @Test
    @DisplayName("❌ 성별이 올바르지 않은 경우")
    void registerUser_invalidGender() {
        // Given
        Userdto userDto = new Userdto("testUser", "pass123", "test@example.com",
                LocalDate.of(2000, 1, 1), "잘못된값", true);

        // When
        String result = userService.registerUser(userDto);

        // Then
        assertEquals("올바르지 않은 성별 값입니다. (남성, 여성, 기타 중 선택)", result);
    }
}
