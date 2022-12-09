package com.example.team9_SpringSecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing  // Audit 시간을 사용하려면 이 어노테이션을 꼭 사용해야한다
@SpringBootApplication
public class team9SpringSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(team9SpringSecurityApplication.class, args);
    }

}