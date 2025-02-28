package com.example.calendar.dto;

import com.example.calendar.entity.Memos;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemosDto {
    private Long memoId; // 메모 ID
    private Long userId; // 유저 ID
    private Long scheduleId; // 일정 ID
    private String memo; // 메모 내용
    private String emotion; // 감정 상태 (String 타입으로 변환)
    private String shareLink; // 공유 링크
    private LocalDateTime createdAt; // 생성 시간

    // ✅ 엔티티 → DTO 변환
    public static MemosDto fromEntity(Memos memos) {
        if (memos == null) {
            return null; // null 방어
        }

        return MemosDto.builder()
                .memoId(memos.getMemoId())
                .userId(memos.getUser() != null ? memos.getUser().getUserId() : null)
                .scheduleId(memos.getSchedule() != null ? memos.getSchedule().getScheduleId() : null)
                .memo(memos.getMemo())
                .emotion(memos.getEmotion() != null ? memos.getEmotion().name() : "보통") // 감정이 없으면 "보통" 기본값
                .shareLink(memos.getShareLink())
                .createdAt(memos.getCreatedAt())
                .build();
    }
}
