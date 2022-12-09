package com.example.team9_SpringSecurity.dto;

import lombok.Getter;

@Getter // Class 모든 필드의 Getter method를 생성
public class MemoRequestDto { // 클라이언트와 서버 사이의 데이터를 옮기는 캐리어 역할을 하는 Dto

    private String title;
    private String content;
    
}
