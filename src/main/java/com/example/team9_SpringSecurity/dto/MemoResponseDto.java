package com.example.team9_SpringSecurity.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MemoResponseDto {

    private Long id;
    private String title;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private List<ReplyResponseDto> replies = new ArrayList<>();

        // Builder 구현해서 사용
        public MemoResponseDto(Long id, String title, String username, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, List<ReplyResponseDto> replies) {
        this.id = id;
        this.title = title;
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.replies = replies;
    }
}
