package com.handwoong.everyonewaiter.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    /**
     * 400 Bad Request
     */
    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "요청 인자가 잘못되었습니다."),

    MEMBER_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 회원입니다."),

    PHONE_NUMBER_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 휴대폰 번호입니다."),

    TELEPHONE_NUMBER_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 전화번호입니다."),

    STORE_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 매장입니다."),

    WAITING_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 웨이팅 정보입니다."),

    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    /**
     * 404 Not Found
     */
    MEMBER_NOT_FOUND(NOT_FOUND, "해당 회원을 찾을 수 없습니다."),

    STORE_NOT_FOUND(NOT_FOUND, "해당 매장을 찾을 수 없습니다."),

    WAITING_NOT_FOUND(NOT_FOUND, "해당 웨이팅 정보를 찾을 수 없습니다."),

    /**
     * 500 Internal Server Error
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생하였습니다.");

    private final HttpStatus status;

    private final String message;
}
