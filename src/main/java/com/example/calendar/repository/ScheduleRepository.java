package com.example.calendar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.calendar.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // 특정 사용자의 일정 목록 조회
    List<Schedule> findByUserUserId(Long userId);

    // 특정 제목의 일정 조회 (제목이 같을 수 있음)
    List<Schedule> findByTitleContaining(String keyword);
}
