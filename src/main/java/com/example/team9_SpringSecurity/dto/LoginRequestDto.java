package com.example.team9_SpringSecurity.dto;

import lombok.Getter;

@Getter
public class LoginRequestDto{
    private String username;     // 사용자 이름
    private String password;     // 사용자 패스워드
}
