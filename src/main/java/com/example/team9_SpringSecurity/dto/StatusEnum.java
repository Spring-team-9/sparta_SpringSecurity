package com.example.team9_SpringSecurity.dto;

import lombok.Getter;

@Getter
public enum StatusEnum {
    OK(200, "OK");
    int statusCode;
    String code;

    StatusEnum(int statusCode, String code) {
        this.statusCode = statusCode;
        this.code = code;
    }
}
