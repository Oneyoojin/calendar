package com.example.calendar.repository;

import com.example.calendar.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);  // 아이디 중복 체크
    Optional<Users> findByEmail(String email);  // 이메일 중복 체크

    // 이메일과 생년월일을 기반으로 사용자 조회
    Optional<Users> findByEmailAndDateOfBirth(String email, LocalDate birthdate);
}
