package com.example.team9_SpringSecurity.util.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
//실제로 유저에게 보낼 응답 Format
public class ErrorResponse {
    private final LocalDateTime timestamp =  LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode){
        return ResponseEntity                           // ErrorCode 를 받아서 ResponseEntity<ErrorResponse> 로 변환
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .status(errorCode.getHttpStatus().value())
                        .error(errorCode.getHttpStatus().name())
                        .code(errorCode.name())
                        .message(errorCode.getDetail())
                        .build()
                );
    }
}
