package com.example.team9_SpringSecurity.dto;

import com.example.team9_SpringSecurity.entity.Reply;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MemoResponseDtoBuilder implements MemoResponseDtoBuilderInterface{
    private Long id;                    // 글 Id
    private String title;               // 제목
    private String username;            // 사용자 이름
    private String content;             // 내용
    private LocalDateTime createdAt;    // 생성일자
    private LocalDateTime modifiedAt;   // 수정일자
    private int totalcnt;               // 글 좋아요 count

    private List<ReplyResponseDto> replies = new ArrayList<>();

    // interface 메서드 재정의
    @Override
    public MemoResponseDtoBuilderInterface id(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public MemoResponseDtoBuilderInterface title(String title) {
        this.title = title;
        return this;
    }

    @Override
    public MemoResponseDtoBuilderInterface username(String username) {
        this.username = username;
        return this;
    }

    @Override
    public MemoResponseDtoBuilderInterface content(String content) {
        this.content = content;
        return this;
    }

    @Override
    public MemoResponseDtoBuilderInterface createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @Override
    public MemoResponseDtoBuilderInterface modifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    @Override
    public MemoResponseDtoBuilder totalcnt (int totalcnt) {
        this.totalcnt = totalcnt;
        return this;
    }

    // 댓글 List 추가 기능
    @Override
    public MemoResponseDtoBuilderInterface addReply(List<Reply> replies) {
        for(int i=0; i<replies.size(); i++){
            this.replies.add(new ReplyResponseDto(replies.get(i)));
        }
        return this;
    }

    @Override
    public MemoResponseDto getMemos() {
        return new MemoResponseDto(id, title, username, content, createdAt, modifiedAt, replies, totalcnt);
    }
}
