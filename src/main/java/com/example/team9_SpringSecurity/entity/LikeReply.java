package com.example.team9_SpringSecurity.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter                                         // Class 모든 필드의 Getter method를 생성
@Entity(name = "LikeReply")                     // Entity임을 선언
@NoArgsConstructor                              // @NoArgsConstructor : 파라미터가 없는 기본 생성자를 생성
public class LikeReply {
    @Id                                         // ID임을 선언
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long LikeId;                        // 좋아요 Id

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;                          // User Entity Join

    @ManyToOne
    @JoinColumn(name = "MEMO_ID", nullable = false)
    private Memo memo;                          // Memo Entity Join

    @ManyToOne
    @JoinColumn(name = "REPLY_ID", nullable = false)
    private Reply reply;                        // Reply Entity Join

    public LikeReply(User user, Memo memo, Reply reply) {
        this.user = user;
        this.memo = memo;
        this.reply = reply;
    }
}
