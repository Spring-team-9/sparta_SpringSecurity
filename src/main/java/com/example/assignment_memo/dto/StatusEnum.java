package com.example.assignment_memo.dto;

import lombok.Getter;

@Getter
public enum StatusEnum {

    //200
    OK(200, "OK");

    int statusCode;
    String code;

    StatusEnum(int statusCode, String code){
        this.statusCode = statusCode;
        this.code = code;
    }
}
