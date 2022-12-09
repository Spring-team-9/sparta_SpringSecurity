package com.example.team9_SpringSecurity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // null값 아닌 것만 Json에 추가하기
public class MessageDto {

    private int status;
    private String message;
    private Object data;

    public MessageDto(){
    }

    public MessageDto(StatusEnum status){
        this.status = status.statusCode;
        this.message = status.code;
    }

    public MessageDto(StatusEnum status, MemoResponseDto dto){
        this.status = status.statusCode;
        this.message = status.code;
        this.data = dto;
    }

    public MessageDto(StatusEnum status, ReplyResponseDto dto){
        this.status = status.statusCode;
        this.message = status.code;
        this.data = dto;
    }


    public MessageDto(StatusEnum status, List<MemoResponseDto> dto){
        this.status = status.statusCode;
        this.message = status.code;
        this.data = dto;
    }
}
