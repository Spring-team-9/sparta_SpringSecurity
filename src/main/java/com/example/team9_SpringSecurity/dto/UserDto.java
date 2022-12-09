package com.example.team9_SpringSecurity.dto;

import com.example.team9_SpringSecurity.entity.User;
import lombok.Getter;

@Getter
public class UserDto extends MessageDto{
    private User user;

    public UserDto(StatusEnum status, User user) {
        super(status);
        this.user = user;
    }
}
