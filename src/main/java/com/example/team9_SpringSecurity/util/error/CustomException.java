package com.example.team9_SpringSecurity.util.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// 전역으로 사용할 CustomException클래스
public class CustomException extends RuntimeException {     //RuntimeException 을 상속받아서 Unchecked Exception 으로 활용. 이걸 상속받으니까 아이콘에 번개생김
    private final ErrorCode errorCode; // Enum이니까

}
