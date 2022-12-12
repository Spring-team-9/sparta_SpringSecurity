package com.example.team9_SpringSecurity.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter                                                             // Class 모든 필드의 Getter method를 생성
@Entity(name = "LikeMemo")                                          // Entity임을 선언
@NoArgsConstructor                                                  // @NoArgsConstructor : 파라미터가 없는 기본 생성자를 생성
public class LikeMemo {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long LikeId;                                            // 좋아요 Id

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "MEMO_ID", nullable = false)
    private Memo memo;

    public LikeMemo(User user, Memo memo) {
        this.user = user;
        this.memo = memo;
    }
}
