package com.example.team9_SpringSecurity.dto;

import lombok.Getter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class SignupRequestDto {

    @Size(min=4, max=10, message = "4자에서 10자 사이")
    @Pattern(regexp="^[a-z0-9]*$", message = "알파벳 소문자 a-z 숫자 0-9 가능")
    private String username;

    @Size(min=8, max=15, message = "8자에서 15자 사이")
    @Pattern(regexp="^[a-zA-Z0-9!@#$%^&+=]*$", message = "알파벳 소문자 a-z 숫자 0-9 특수문자 가능")
    private String password;

    private String adminToken;

}
