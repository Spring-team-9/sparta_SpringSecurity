package com.example.team9_SpringSecurity.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "users")
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING) // Enum을 사용하며 어떤 방식으로 사용할지 설정 // 해당 문자열 그대로 사용
    private UserRoleEnum role;

    @OneToMany(mappedBy = "user")
    private List<Memo> memo;

    public User(String username, String password, UserRoleEnum role){
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
