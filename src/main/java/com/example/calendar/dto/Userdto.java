package com.example.calendar.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Userdto {
    private String username;  // 사용자 이름
    private String password;  // 비밀번호
    private String email;     // 이메일
    private String birthdate; // 생년월일
    private String gender;    // 성별
    private String nationality;  // 국적
}
