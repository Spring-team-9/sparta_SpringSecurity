package com.example.team9_SpringSecurity.dto;

import com.example.team9_SpringSecurity.entity.Reply;
import lombok.Getter;

@Getter
public class ReplyResponseDto {
    private Long replyId;                         // 댓글 Id
    private Long memoId;                          // 글 Id
    private String replyName;                     // 사용자 Id
    private String replyContent;                  // 댓글 내용
    private int totalcnt;                         // 댓글 좋아요 cnt

    public ReplyResponseDto(Reply reply) {
        this.replyId = reply.getReplyId();
        this.memoId = reply.getMemo().getMemoId();
        this.replyName = reply.getReplyName();
        this.replyContent = reply.getReplyContent();
        this.totalcnt = reply.getTotalCommentCount();
    }
}
