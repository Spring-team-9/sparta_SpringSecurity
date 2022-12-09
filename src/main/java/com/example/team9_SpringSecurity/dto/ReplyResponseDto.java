package com.example.team9_SpringSecurity.dto;

import com.example.team9_SpringSecurity.entity.Reply;
import lombok.Getter;

@Getter
public class ReplyResponseDto {

    private Long replyId;
    private Long memoId;
    private String replyName;
    private String replyContent;

    public ReplyResponseDto(Reply reply) {
        this.replyId = reply.getReplyId();
        this.memoId = reply.getMemo().getMemoId();
        this.replyName = reply.getReplyName();
        this.replyContent = reply.getReplyContent();
    }
}
