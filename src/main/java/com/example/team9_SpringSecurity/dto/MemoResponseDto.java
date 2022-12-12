package com.example.team9_SpringSecurity.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MemoResponseDto {

    private Long id;                                        // 글 Id
    private String title;                                   // 제목 Id
    private String username;                                // 사용자 이름
    private String content;                                 // 내용
    private LocalDateTime createdAt;                        // 생성일자
    private LocalDateTime modifiedAt;                       // 수정일자
    private int totalcnt;                                   // 글 좋아요 cnt
    private List<ReplyResponseDto> replies = new ArrayList<>();    // 댓글 List

    // Builder 구현해서 사용
    public MemoResponseDto(Long id, String title, String username, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, List<ReplyResponseDto> replies, int totalcnt) {
        this.id = id;                           // 글 Id
        this.title = title;                     // 제목 Id
        this.username = username;               // 사용자 이름
        this.content = content;                 // 내용
        this.createdAt = createdAt;             // 생성일자
        this.modifiedAt = modifiedAt;           // 수정일자
        this.replies = replies;                 // 댓글
        this.totalcnt = totalcnt;               // 글 좋아요 cnt
    }
}
