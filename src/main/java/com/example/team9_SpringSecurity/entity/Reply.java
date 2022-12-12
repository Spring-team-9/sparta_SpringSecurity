package com.example.team9_SpringSecurity.entity;

import com.example.team9_SpringSecurity.dto.ReplyRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Reply extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long replyId;                               // 댓글 Id

    @ManyToOne
    @JoinColumn(name = "MEMO_ID", nullable = false)
    private Memo memo;                                  // Memo Entity Join

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;                                  // Memo Entity Join

    @Column(nullable = false)
    private String replyName;                           // 댓글 작성자 Id

    @Column(nullable = false)
    private String replyContent;                        // 댓글 내용

    @OneToMany(mappedBy = "reply", cascade = CascadeType.REMOVE)     // 연관테이블(하위테이블) 삭제 처리
    private List<LikeReply> likereply = new ArrayList<>();

    @Formula("(select count(1) from like_Reply where like_Reply.reply_id = reply_id)")       // 댓글 좋아요 cnt를 위한 select 문 사용
    private int totalCommentCount;

    public Reply(ReplyRequestDto dto, User user, Memo memo){
        this.memo = memo;
        this.replyName = user.getUsername();
        this.replyContent = dto.getReplyContent();
        this.user = user;
        this.totalCommentCount = getTotalCommentCount();
    }

    public void update(ReplyRequestDto dto){
        this.replyContent = dto.getReplyContent();
    }
}
