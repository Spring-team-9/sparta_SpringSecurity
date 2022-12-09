package com.example.assignment_memo.dto;

import com.example.assignment_memo.entity.User;
import lombok.Getter;

@Getter
public class UserDto extends MessageDto{
    private User user;

    public UserDto(StatusEnum status, User user) {
        super(status);
        this.user = user;
    }
}
