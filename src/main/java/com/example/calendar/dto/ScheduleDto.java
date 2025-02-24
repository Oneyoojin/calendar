package com.example.calendar.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ScheduleDto {
    private Long scheduleId; // 스케줄 고유 ID
    private Long userId; // 유저 ID
    private String todoList; // 할 일 기본 목록
    private String title; // 제목
    private String description; // 일정 상세 설명
    private LocalDateTime startTime; // 시작 시간
    private LocalDateTime endTime; // 끝나는 시간
    private LocalDateTime reminderTime; // 알림 시간
    private Boolean priority; // 중요 여부 (true: 높은 우선순위, false: 기본)
    private Boolean status; // 진행 상태 (true: 활성, false: 완료)
    private LocalDateTime completedAt; // 완료된 시간
    private String shareLink; // 일정 공유 링크
    private LocalDateTime createdAt; // 일정 생성 시간
    private LocalDateTime updatedAt; // 마지막 수정 시간
}
