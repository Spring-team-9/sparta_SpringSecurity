package com.example.team9_SpringSecurity.util.error;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.example.team9_SpringSecurity.util.error.ErrorCode.DUPLICATE_RESOURCE;

@Slf4j                  // 로깅
@RestControllerAdvice   // @ControllerAdvice 는 프로젝트 전역에서 발생하는 모든 예외를 잡아준다
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { ConstraintViolationException.class, DataIntegrityViolationException.class}) // @ExceptionHandler 는 발생한 특정 예외를 잡아서 하나의 메소드에서 공통 처리할 수 있게 한다
    protected ResponseEntity<ErrorResponse> handleDataException() {                                         // handleDataException 메소드에서는 hibernate 관련 에러
        log.error("handleDataException throw Exception : {}", DUPLICATE_RESOURCE);
        return ErrorResponse.toResponseEntity(DUPLICATE_RESOURCE); //에러코드
    }

    @ExceptionHandler(value = { CustomException.class })                                                    // handleCustomException 메소드는 직접 정의한 CustomException 을 사용
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {                      // Exception 발생 시 넘겨받은 ErrorCode 를 사용해서 사용자에게 보여주는 에러 메세지를 정의
        log.error("handleCustomException throw CustomException : {}", e.getErrorCode());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
}
