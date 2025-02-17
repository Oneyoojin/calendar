package com.example.calendar.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Userdto {
    private String username;
    private String password;
    private String email;
    private LocalDate birthdate;
    private String gender;
    private boolean isDomestic;
}
