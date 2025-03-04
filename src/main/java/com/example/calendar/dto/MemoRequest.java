package com.example.calendar.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemoRequest {
    private Long scheduleId; // 일정 ID
    private String memoContent; // 메모 내용
}
