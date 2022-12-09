package com.example.team9_SpringSecurity.repository;


import com.example.team9_SpringSecurity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Optional은 객체를 포장해주는 래퍼클래스로 null값도 받을 수 있음
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndPassword(String username, String password);
}

