package com.example.calendar.repository;

import com.example.calendar.entity.Memos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemosRepository extends JpaRepository<Memos, Long> {
    
    // ✅ 특정 사용자의 메모 목록 조회
    List<Memos> findByUserUserId(Long userId);

    // ✅ 특정 일정의 메모 목록 조회
    List<Memos> findByScheduleScheduleId(Long scheduleId);
}
