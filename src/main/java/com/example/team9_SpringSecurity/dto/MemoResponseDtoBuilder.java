package com.example.team9_SpringSecurity.dto;

import com.example.team9_SpringSecurity.entity.Reply;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MemoResponseDtoBuilder implements MemoResponseDtoBuilderInterface{

    private Long id;
    private String title;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<ReplyResponseDto> replies = new ArrayList<>();


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
    public MemoResponseDtoBuilderInterface addReply(List<Reply> replies) {
        for(int i=0; i<replies.size(); i++){
            this.replies.add(new ReplyResponseDto(replies.get(i)));
        }
        return this;
    }

    @Override
    public MemoResponseDto getMemos() {
        return new MemoResponseDto(id, title, username, content, createdAt, modifiedAt, replies);
    }
}
