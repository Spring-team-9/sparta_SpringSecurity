package com.example.team9_SpringSecurity.entity;

import com.example.team9_SpringSecurity.dto.MemoRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter                                                         // Class 모든 필드의 Getter method를 생성
@Entity                                                         // Entity임을 선언
@NoArgsConstructor                                              // @NoArgsConstructor : 파라미터가 없는 기본 생성자를 생성
public class Memo extends Timestamped {

    @Id                                                         // ID임을 선언
    @Column(name = "MEMO_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memoId;                                        // 글 Id

    @Column(nullable = false)
    private String title;                                       // 제목

    @Column(nullable = false)
    private String username;                                    // 사용자 이름

    @Column(nullable = false)
    private String content;                                     // 내용

    @ManyToOne
    @JoinColumn(name = "user_id")                               // User Entity Join
    private User user;

    @OneToMany(mappedBy = "memo", cascade = CascadeType.REMOVE)         // 상위테이블 삭제 시 연관테이블(하위테이블) 삭제 처리

    @OrderBy("createdAt desc")                                          // 내림차순으로 정렬
    private List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "memo", cascade = CascadeType.REMOVE)         // 상위테이블 삭제 시 연관테이블(하위테이블) 삭제 처리
    private List<LikeMemo> likes = new ArrayList<>();

    @Formula("(select count(1) from like_memo where like_memo.memo_id = memo_id)")     // 게시물 좋아요 cnt를 위한 select 문 사용
    private int totalCommentCount;

    public Memo(MemoRequestDto dto, User user) {
        this.title = dto.getTitle();
        this.username = user.getUsername();
        this.content = dto.getContent();
        this.user = user;
        this.totalCommentCount = getTotalCommentCount();
    }

    public void update(MemoRequestDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
    }

}
