package com.example.team9_SpringSecurity.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class ReplyLike {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "memo_id")
    private Memo memo;

    @ManyToOne
    @JoinColumn(name = "reply_id")
    private Reply reply;


    public ReplyLike(User user, Memo memo, Reply reply){
        this.user = user;
        this.memo = memo;
        this.reply = reply;
    }
}
