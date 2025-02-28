package com.example.calendar.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "schedules", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "start_time", "end_time"})
}, indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_start_time", columnList = "start_time"),
        @Index(name = "idx_status", columnList = "status")
})
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId; // 일정 고유 ID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user; // 유저 ID (참조 관계)

    @Column(name = "to_do_list", length = 100, nullable = false)
    private String todoList = "기본 목록"; // 할 일 기본 목록 (기본값: "기본 목록")

    @Column(name = "title", length = 255, nullable = false)
    private String title; // 제목

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 일정 상세 설명

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime; // 시작 시간

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime; // 끝나는 시간

    @Column(name = "reminder_time")
    private LocalDateTime reminderTime; // 알림 시간

    @Column(name = "priority", nullable = false)
    private Boolean priority = false; // 중요 여부 (기본값: false)

    @Column(name = "status", nullable = false)
    private Boolean status = true; // 진행 상태 (기본값: true)

    @Column(name = "completed_at")
    private LocalDateTime completedAt; // 완료된 시간

    @Column(name = "share_link", unique = true, length = 36)
    private String shareLink; // 일정 공유 링크

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 일정 생성 시간

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 마지막 수정 시간

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
