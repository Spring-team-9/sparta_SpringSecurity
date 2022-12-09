package com.example.team9_SpringSecurity.entity;

import com.example.team9_SpringSecurity.dto.MemoRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter // Class 모든 필드의 Getter method를 생성
@Entity // Entity임을 선언
@NoArgsConstructor // @NoArgsConstructor : 파라미터가 없는 기본 생성자를 생성
public class Memo extends Timestamped {

    @Id     // ID임을 선언
    @Column(name = "MEMO_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memoId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "memo", cascade = CascadeType.REMOVE) // 글 하나가 삭제되면 맵핑되어있는 쪽 테이블이름!!! 글도 삭제되는 cascade 연속성 전이 속성
    @OrderBy("createdAt desc ")                                 // 엔티티단에서 정렬
    private List<Reply> replies = new ArrayList<>();            // 일대다의 다 부분을 List로 받기


    public Memo(MemoRequestDto dto, User user){

        this.title = dto.getTitle();
        this.username = user.getUsername();
        this.content = dto.getContent();
        this.user = user;

    }

    public void update(MemoRequestDto dto){

        this.title = dto.getTitle();
        this.content = dto.getContent();
    }


}
