package com.example.calendar;

import com.example.calendar.dto.Userdto;
import com.example.calendar.entity.Users;
import com.example.calendar.repository.UsersRepository;
import com.example.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private Userdto validUserDto;

    @BeforeEach
    void setUp() {
        validUserDto = new Userdto();
        validUserDto.setUsername("testuser");
        validUserDto.setPassword("password123");
        validUserDto.setEmail("test@example.com");
        validUserDto.setBirthdate("2000-05-15"); // 올바른 생년월일
        validUserDto.setGender("남성"); // 올바른 성별
        validUserDto.setNationality("대한민국");
    }

    /**
     * ✅ 회원가입 성공 테스트
     */
    @Test
    void testRegisterUser_Success() {
        // GIVEN: 중복 아이디가 없음
        when(usersRepository.findByUsername(validUserDto.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(validUserDto.getPassword())).thenReturn("encodedPassword");

        // WHEN: 회원가입 요청
        String result = userService.registerUser(validUserDto);

        // THEN: 회원가입 성공 메시지 확인
        assertEquals("회원가입 성공!", result);
        verify(usersRepository, times(1)).save(any(Users.class)); // DB에 저장했는지 확인
    }

    /**
     * ✅ 중복 아이디로 인한 회원가입 실패 테스트
     */
    @Test
    void testRegisterUser_DuplicateUsername() {
        // GIVEN: 중복 아이디가 존재함
        when(usersRepository.findByUsername(validUserDto.getUsername())).thenReturn(Optional.of(new Users()));

        // WHEN: 회원가입 요청
        String result = userService.registerUser(validUserDto);

        // THEN: 이미 가입된 아이디 메시지 반환
        assertEquals("이미 가입된 아이디입니다.", result);
        verify(usersRepository, never()).save(any(Users.class)); // 저장 시도 안 해야 함
    }

    /**
     * ❌ 잘못된 생년월일로 인한 회원가입 실패 테스트
     */
    @Test
    void testRegisterUser_InvalidBirthdate() {
        // GIVEN: 잘못된 생년월일 입력
        validUserDto.setBirthdate("2025-13-45"); // 존재하지 않는 날짜

        // WHEN: 회원가입 요청
        String result = userService.registerUser(validUserDto);

        // THEN: 생년월일 오류 메시지 확인
        assertEquals("생년월일 형식이 올바르지 않습니다. (예: 2000-01-01)", result);
        verify(usersRepository, never()).save(any(Users.class));
    }

    /**
     * ❌ 잘못된 성별로 인한 회원가입 실패 테스트
     */
    @Test
    void testRegisterUser_InvalidGender() {
        // GIVEN: 존재하지 않는 성별 입력
        validUserDto.setGender("Unknown"); // ENUM에 없는 값

        // WHEN: 회원가입 요청
        String result = userService.registerUser(validUserDto);

        // THEN: 성별 오류 메시지 확인
        assertEquals("올바르지 않은 성별 값입니다. (남성, 여성, 기타 중 선택)", result);
        verify(usersRepository, never()).save(any(Users.class));
    }
}
