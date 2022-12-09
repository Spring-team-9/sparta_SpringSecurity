package com.example.team9_SpringSecurity.entity;

import com.example.team9_SpringSecurity.dto.ReplyRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Reply extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long replyId;


    @ManyToOne
    @JoinColumn(name = "MEMO_ID", nullable = false)
    private Memo memo; //필드 하나가 아니라 객체로 연결해야함

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String replyName;

    @Column(nullable = false)
    private String replyContent;

    public Reply(ReplyRequestDto dto, User user, Memo memo){
        this.memo = memo;
        this.replyName = user.getUsername();
        this.replyContent = dto.getReplyContent();
        this.user = user;
    }

    public void update(ReplyRequestDto dto){
        this.replyContent = dto.getReplyContent();
    }
}
