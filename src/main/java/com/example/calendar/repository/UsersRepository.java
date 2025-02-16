package com.example.calendar.repository;

import com.example.calendar.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);  // username으로 사용자 검색
}
