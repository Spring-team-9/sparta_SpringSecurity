package com.example.team9_SpringSecurity.dto;

import java.time.LocalDateTime;
import java.util.List;

public interface MemoResponseDtoBuilderInterface {

    MemoResponseDtoBuilderInterface id(Long id);

    MemoResponseDtoBuilderInterface title(String title);

    MemoResponseDtoBuilderInterface username(String username);

    MemoResponseDtoBuilderInterface content(String content);

    MemoResponseDtoBuilderInterface likeCnt(Long cnt);

    MemoResponseDtoBuilderInterface createdAt(LocalDateTime createdAt);

    MemoResponseDtoBuilderInterface modifiedAt(LocalDateTime modifiedAt);

    MemoResponseDtoBuilderInterface addReply(List<ReplyResponseDto> replies);

    MemoResponseDto getMemos();
}
