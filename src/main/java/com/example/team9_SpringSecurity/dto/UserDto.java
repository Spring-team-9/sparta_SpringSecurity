package com.example.team9_SpringSecurity.dto;

import com.example.team9_SpringSecurity.entity.User;
import lombok.Getter;

@Getter
public class UserDto extends MessageDto{
    private User user;          // User Table

    public UserDto(StatusEnum status, User user) {
        super(status);          // Status Code 상속
        this.user = user;       // UserTable
    }
}
