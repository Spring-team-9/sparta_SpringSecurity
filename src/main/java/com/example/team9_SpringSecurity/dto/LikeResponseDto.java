package com.example.team9_SpringSecurity.dto;

import com.example.team9_SpringSecurity.entity.LikeMemo;
import com.example.team9_SpringSecurity.entity.LikeReply;
import lombok.Getter;

@Getter
public class LikeResponseDto {
    private Long LikeId;    // 좋아요 Id
    private Long memoId;    // 게시물 Id
    private Long userId;    // 사용자 Id
    private Long replyId;   // 댓글 Id

    public LikeResponseDto(LikeMemo likeMemo) {             // 글 좋아요
        this.LikeId = likeMemo.getLikeId();
        this.memoId = likeMemo.getMemo().getMemoId();
        this.userId = likeMemo.getUser().getId();
    }

    public LikeResponseDto(LikeReply likeReply) {
        this.LikeId = likeReply.getLikeId();
        this.replyId = likeReply.getReply().getReplyId();
        this.memoId = likeReply.getMemo().getMemoId();
        this.userId = likeReply.getUser().getId();
    }
}
