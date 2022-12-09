package com.example.team9_SpringSecurity.dto;

import com.example.team9_SpringSecurity.entity.Reply;

import java.time.LocalDateTime;
import java.util.List;

public interface MemoResponseDtoBuilderInterface {

    MemoResponseDtoBuilderInterface id(Long id);

    MemoResponseDtoBuilderInterface title(String title);

    MemoResponseDtoBuilderInterface username(String username);

    MemoResponseDtoBuilderInterface content(String content);

    MemoResponseDtoBuilderInterface createdAt(LocalDateTime createdAt);

    MemoResponseDtoBuilderInterface modifiedAt(LocalDateTime modifiedAt);

    MemoResponseDtoBuilderInterface addReply(List<Reply> replies);

    MemoResponseDto getMemos();
}
