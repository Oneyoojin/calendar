package com.example.service;

import com.example.calendar.dto.MemoRequest;
import com.example.calendar.dto.MemosDto;
import com.example.calendar.entity.Memos;
import com.example.calendar.entity.Schedule;
import com.example.calendar.entity.Users;
import com.example.calendar.repository.MemosRepository;
import com.example.calendar.repository.ScheduleRepository;
import com.example.calendar.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemosService {

    private final MemosRepository memosRepository;
    private final UsersRepository usersRepository;
    private final ScheduleRepository scheduleRepository;

    // ✅ 메모 추가 (MemoRequest 기반)
    @Transactional
    public MemosDto addMemo(MemoRequest request, Long userId) {
        log.info("📌 [addMemo 요청] userId: {}, scheduleId: {}, memoContent={}", 
                 userId, request.getScheduleId(), request.getMemoContent());

        // 사용자 조회
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("❌ [addMemo 오류] 사용자를 찾을 수 없습니다. userId: {}", userId);
                    return new RuntimeException("❌ 사용자를 찾을 수 없습니다. userId: " + userId);
                });

        // 일정 조회
        log.info("🔎 [DB 조회] schedule_id={} 확인 중...", request.getScheduleId());
        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> {
                    log.error("❌ [addMemo 오류] DB에서 일정을 찾을 수 없습니다. scheduleId: {}", request.getScheduleId());
                    return new RuntimeException("❌ DB에서 일정을 찾을 수 없습니다. scheduleId: " + request.getScheduleId());
                });

        log.info("✅ [DB 조회 완료] 사용자: {}, 일정: {}", user.getUsername(), schedule.getTitle());

        // 메모 엔티티 생성 및 저장
        Memos memo = Memos.builder()
                .user(user)
                .schedule(schedule)
                .memo(request.getMemoContent())
                .emotion(Memos.Emotion.NEUTRAL)
                .shareLink(null)
                .createdAt(LocalDateTime.now())
                .build();

        Memos savedMemo = memosRepository.save(memo);
        log.info("✅ [메모 저장 완료] memoId: {}, 내용: {}", savedMemo.getMemoId(), savedMemo.getMemo());

        return MemosDto.fromEntity(savedMemo);
    }

    // ✅ 메모 추가 (MemosDto 기반) - **한글 emotion 처리 추가**
    @Transactional
    public MemosDto addMemo(MemosDto memoDto) {
        log.info("📌 [addMemo 요청] userId: {}, scheduleId: {}, memoContent={}, emotion={}", 
                 memoDto.getUserId(), memoDto.getScheduleId(), memoDto.getMemo(), memoDto.getEmotion());

        // 사용자 조회
        Users user = usersRepository.findById(memoDto.getUserId())
                .orElseThrow(() -> {
                    log.error("❌ [addMemo 오류] 사용자를 찾을 수 없습니다. userId: {}", memoDto.getUserId());
                    return new RuntimeException("❌ 사용자를 찾을 수 없습니다. userId: " + memoDto.getUserId());
                });

        // 일정 조회
        log.info("🔎 [DB 조회] schedule_id={} 확인 중...", memoDto.getScheduleId());
        Schedule schedule = scheduleRepository.findById(memoDto.getScheduleId())
                .orElseThrow(() -> {
                    log.error("❌ [addMemo 오류] DB에서 일정을 찾을 수 없습니다. scheduleId: {}", memoDto.getScheduleId());
                    return new RuntimeException("❌ DB에서 일정을 찾을 수 없습니다. scheduleId: " + memoDto.getScheduleId());
                });

        log.info("✅ [DB 조회 완료] 사용자: {}, 일정: {}", user.getUsername(), schedule.getTitle());

        // ✅ emotion 값 변환 (한글 → Enum)
        String emotionValue = memoDto.getEmotion();
        if (emotionValue == null || emotionValue.trim().isEmpty()) {
            emotionValue = "NEUTRAL"; // 기본값 설정
        } else if ("보통".equals(emotionValue)) {
            emotionValue = "NEUTRAL"; // 한글 값을 NEUTRAL로 변환
        }

        // 유효한 Enum 값인지 검증 후 변환
        Memos.Emotion emotionEnum;
        try {
            emotionEnum = Memos.Emotion.valueOf(emotionValue);
        } catch (IllegalArgumentException e) {
            log.warn("⚠️ [addMemo 경고] 잘못된 emotion 값 '{}' → 기본값 'NEUTRAL' 적용", emotionValue);
            emotionEnum = Memos.Emotion.NEUTRAL; // 기본값 적용
        }

        // ✅ 메모 엔티티 생성 및 저장
        Memos memo = Memos.builder()
                .user(user)
                .schedule(schedule)
                .memo(memoDto.getMemo())
                .emotion(emotionEnum) // 변환된 emotion 값 적용
                .shareLink(memoDto.getShareLink())
                .createdAt(LocalDateTime.now())
                .build();

        Memos savedMemo = memosRepository.save(memo);
        log.info("✅ [메모 저장 완료] memoId: {}, 내용: {}, emotion={}", savedMemo.getMemoId(), savedMemo.getMemo(), savedMemo.getEmotion());

        return MemosDto.fromEntity(savedMemo);
    }

    // ✅ 특정 유저의 메모 목록 조회
    @Transactional(readOnly = true)
    public List<MemosDto> getMemosByUserId(Long userId) {
        log.info("📌 [getMemosByUserId 요청] userId: {}", userId);
        return memosRepository.findByUserUserId(userId)
                .stream()
                .map(MemosDto::fromEntity)
                .collect(Collectors.toList());
    }

    // ✅ 특정 일정의 메모 목록 조회
    @Transactional(readOnly = true)
    public List<MemosDto> getMemosByScheduleId(Long scheduleId) {
        log.info("📌 [getMemosByScheduleId 요청] scheduleId: {}", scheduleId);
        return memosRepository.findByScheduleScheduleId(scheduleId)
                .stream()
                .map(MemosDto::fromEntity)
                .collect(Collectors.toList());
    }

    // ✅ 특정 메모 조회
    @Transactional(readOnly = true)
    public MemosDto getMemoById(Long memoId) {
        log.info("📌 [getMemoById 요청] memoId: {}", memoId);
        return memosRepository.findById(memoId)
                .map(MemosDto::fromEntity)
                .orElseThrow(() -> new RuntimeException("❌ 해당 ID의 메모를 찾을 수 없습니다. memoId: " + memoId));
    }

    // ✅ 메모 삭제
    @Transactional
    public void deleteMemo(Long memoId) {
        log.info("📌 [deleteMemo 요청] memoId: {}", memoId);

        if (!memosRepository.existsById(memoId)) {
            log.error("❌ [deleteMemo 오류] 해당 메모를 찾을 수 없습니다. memoId: {}", memoId);
            throw new RuntimeException("❌ 해당 메모를 찾을 수 없습니다. memoId: " + memoId);
        }

        memosRepository.deleteById(memoId);
        log.info("✅ [메모 삭제 완료] memoId: {}", memoId);
    }
}
