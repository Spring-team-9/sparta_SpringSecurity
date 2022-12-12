package com.example.team9_SpringSecurity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // null값 아닌 것만 Json에 추가하기
public class MessageDto {
    private int status;             // 상태 코드
    private String message;         // 상태 메세지
    private Object data;            // Reseponse Dto 담을 Object

    public MessageDto() {
    }

    public MessageDto(StatusEnum status) {
        this.status = status.statusCode;
        this.message = status.code;
    }

    public MessageDto(StatusEnum status, MemoResponseDto dto) {
        this.status = status.statusCode;
        this.message = status.code;
        this.data = dto;
    }

    public MessageDto(StatusEnum status, ReplyResponseDto dto) {
        this.status = status.statusCode;
        this.message = status.code;
        this.data = dto;
    }

    public MessageDto(StatusEnum status, LikeResponseDto dto) {
        this.status = status.statusCode;
        this.message = status.code;
        this.data = dto;
    }

    public MessageDto(StatusEnum status, List<MemoResponseDto> dto) {
        this.status = status.statusCode;
        this.message = status.code;
        this.data = dto;
    }

    public MessageDto(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
