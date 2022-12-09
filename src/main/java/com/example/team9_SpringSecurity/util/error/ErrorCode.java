package com.example.team9_SpringSecurity.util.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 200
    OK(HttpStatus.OK, "OK"),

    //400 BAD_REQUEST : 잘못된 요청
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST"),
    BAD_REQUEST_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."),
    EXIST_USER(HttpStatus.BAD_REQUEST, "중복된 username입니다."),
    NO_ACCESS(HttpStatus.BAD_REQUEST, "작성자만 삭제/수정할 수 있습니다."),
    NOT_FOUND(HttpStatus.BAD_REQUEST, "NOT_FOUND"),
    LOGIN_MATCH_FAIL(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),


    // 401 UNAUTHORIZED : 인증되지 않은 사용자
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다"),
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "현재 내 계정 정보가 존재하지 않습니다"),

    // 404 NOT_FOUND : Resource 를 찾을 수 없음
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다"),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "로그아웃 된 사용자입니다"),
    NOT_FOLLOW(HttpStatus.NOT_FOUND, "팔로우 중이지 않습니다"),
    MEMO_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 글이 존재하지 않습니다."),

    //409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "데이터가 이미 존재합니다"),

    //500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");

    private final HttpStatus httpStatus;
    private final String detail;
}

