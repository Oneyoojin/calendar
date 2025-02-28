package com.example.calendar.dto;

import com.example.calendar.entity.Schedule;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDto {
    private Long scheduleId; // 일정 ID
    private Long userId; // 유저 ID
    private String todoList; // 할 일 목록
    private String title; // 제목
    private String description; // 일정 설명
    private LocalDateTime startTime; // 시작 시간
    private LocalDateTime endTime; // 끝나는 시간
    private LocalDateTime reminderTime; // 알림 시간
    private Boolean priority; // 우선순위
    private Boolean status; // 진행 상태
    private LocalDateTime completedAt; // 완료된 시간
    private String shareLink; // 공유 링크
    private LocalDateTime createdAt; // 생성 시간
    private LocalDateTime updatedAt; // 수정 시간

    // ✅ 엔티티 → DTO 변환
    public static ScheduleDto fromEntity(Schedule schedule) {
        if (schedule == null) {
            return null; // null 방어
        }

        return ScheduleDto.builder()
                .scheduleId(schedule.getScheduleId())
                .userId(schedule.getUser() != null ? schedule.getUser().getUserId() : null)
                .todoList(schedule.getTodoList())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .reminderTime(schedule.getReminderTime())
                .priority(schedule.getPriority())
                .status(schedule.getStatus())
                .completedAt(schedule.getCompletedAt())
                .shareLink(schedule.getShareLink())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }
}
