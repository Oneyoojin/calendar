package com.example.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.calendar.entity.Users;
import java.time.LocalDate;

public interface UserRepository extends JpaRepository<Users, Long> {

}
