package com.example.calendar.repository;

import com.example.calendar.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    // 기본적인 CRUD 메서드는 JpaRepository에서 제공됨
}