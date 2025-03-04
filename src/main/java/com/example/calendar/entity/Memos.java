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
@Table(name = "memos", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_schedule_id", columnList = "schedule_id")
})
public class Memos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private Long memoId; // 메모 고유 ID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user; // 유저 ID (참조 관계)

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule; // 일정 ID (참조 관계)

    @Column(name = "memo", columnDefinition = "TEXT")
    private String memo; // 메모 내용

    @Enumerated(EnumType.STRING)
    @Column(name = "emotion", nullable = false)
    private Emotion emotion = Emotion.NEUTRAL; // 감정 상태 (기본값 설정)

    @Column(name = "share_link", unique = true, length = 36)
    private String shareLink; // 공유 링크

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 메모 생성 시간

    // ✅ 감정 상태 Enum
    public enum Emotion {
        HAPPY("기쁨"),
        SAD("슬픔"),
        NEUTRAL("보통"); // 기본값

        private final String koreanName;

        Emotion(String koreanName) {
            this.koreanName = koreanName;
        }

        // ✅ 한글 → Enum 변환 메서드
        public static Emotion fromKoreanName(String koreanName) {
            for (Emotion emotion : Emotion.values()) {
                if (emotion.koreanName.equals(koreanName)) {
                    return emotion;
                }
            }
            return NEUTRAL; // 기본값 설정
        }
    }
}
