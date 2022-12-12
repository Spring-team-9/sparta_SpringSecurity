package com.example.team9_SpringSecurity.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Likes {
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

    public Likes(User user, Memo memo){
        this.user = user;
        this.memo = memo;
    }

    public Likes(User user, Reply reply){
        this.user = user;
        this.reply = reply;
    }
}
