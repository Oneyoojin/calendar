package com.example.calendar;

import com.example.calendar.dto.Userdto;
import com.example.calendar.entity.Users;
import com.example.calendar.repository.UsersRepository;
import com.example.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Mockito 초기화
    }

    @Test
    void testRegisterUser_Success() {
        // GIVEN: 정상적인 회원가입 정보
        Userdto userDto = Userdto.builder()
                .username("testUser")
                .password("password123")
                .email("test@example.com")
                .birthdate("2000-01-01")
                .gender("남성")
                .nationality("대한민국")
                .build();

        when(usersRepository.findByUsername("testUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        // WHEN: 회원가입 요청
        String result = userService.registerUser(userDto);

        // THEN: 회원가입 성공 메시지 확인
        assertEquals("회원가입 성공!", result);
        verify(usersRepository, times(1)).save(any(Users.class));
    }

    @Test
    void testRegisterUser_DuplicateUsername() {
        // GIVEN: 이미 존재하는 아이디
        Userdto userDto = Userdto.builder()
                .username("existingUser")
                .password("password123")
                .email("test@example.com")
                .birthdate("2000-01-01")
                .gender("남성")
                .nationality("대한민국")
                .build();

        when(usersRepository.findByUsername("existingUser")).thenReturn(Optional.of(new Users()));

        // WHEN: 중복 아이디로 회원가입 요청
        String result = userService.registerUser(userDto);

        // THEN: 중복 가입 오류 메시지 확인
        assertEquals("이미 가입된 아이디입니다.", result);
        verify(usersRepository, never()).save(any(Users.class));
    }

    @Test
    void testRegisterUser_InvalidBirthdate() {
        // GIVEN: 잘못된 생년월일 형식
        Userdto userDto = Userdto.builder()
                .username("testUser")
                .password("password123")
                .email("test@example.com")
                .birthdate("2000-99-99")  // 잘못된 날짜
                .gender("남성")
                .nationality("대한민국")
                .build();

        // WHEN: 회원가입 요청
        String result = userService.registerUser(userDto);

        // THEN: 생년월일 오류 메시지 확인
        assertEquals("생년월일 형식이 올바르지 않습니다. (예: 2000-01-01)", result);
        verify(usersRepository, never()).save(any(Users.class));
    }

    @Test
    void testRegisterUser_InvalidGender() {
        // GIVEN: 잘못된 성별 입력
        Userdto userDto = Userdto.builder()
                .username("testUser")
                .password("password123")
                .email("test@example.com")
                .birthdate("2000-01-01")
                .gender("잘못된값")  // 유효하지 않은 값
                .nationality("대한민국")
                .build();

        // WHEN: 회원가입 요청
        String result = userService.registerUser(userDto);

        // THEN: 성별 오류 메시지 확인
        assertEquals("올바르지 않은 성별 값입니다. (남성, 여성, 기타 중 선택)", result);
        verify(usersRepository, never()).save(any(Users.class));
    }
}
