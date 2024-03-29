package com.handwoong.everyonewaiter.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.*

enum class ErrorCode(
    val status: HttpStatus,
    val message: String,
) {

    /**
     * 400 Bad Request
     */
    METHOD_ARGUMENT_NOT_VALID(BAD_REQUEST, "요청 인자가 잘못되었습니다."),

    USERNAME_OR_PHONE_EXISTS(BAD_REQUEST, "이미 존재하는 회원이거나 휴대폰 번호입니다."),

    CHECK_USERNAME_AND_PASSWORD(BAD_REQUEST, "아이디와 비밀번호를 확인 해주세요."),

    PHONE_EXISTS(BAD_REQUEST, "이미 존재하는 휴대폰 번호입니다."),

    TELEPHONE_EXISTS(BAD_REQUEST, "이미 존재하는 전화번호입니다."),

    ORDER_NOT_AVAILABLE_STATUS(BAD_REQUEST, "변경 불가능한 주문 상태 입니다."),

    WAITING_NOT_AVAILABLE_STATUS(BAD_REQUEST, "변경 불가능한 웨이팅 상태 입니다."),

    ORDER_MENU_NOT_AVAILABLE_STATUS(BAD_REQUEST, "변경 불가능한 주문 메뉴 상태 입니다."),

    TABLE_NUMBER_NOT_VALID(BAD_REQUEST, "유효하지 않은 테이블 번호 입니다."),

    NOT_MATCH_PASSWORD(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    NOT_VALID_PAYMENT_PRICE(BAD_REQUEST, "결제 금액 정보가 잘못되었습니다."),

    ALREADY_PROCEEDING_PAYMENT(BAD_REQUEST, "이미 진행 중인 결제가 있습니다."),

    IS_SOLD_OUT_MENU(BAD_REQUEST, "SOLD OUT 메뉴가 포함되어 있습니다."),

    /**
     * 401 Un Authorized
     */
    UN_AUTHORIZED(UNAUTHORIZED, "인증에 실패하였습니다."),

    IN_VALID_TOKEN(UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    /**
     * 404 Not Found
     */
    ID_RESOURCE_NOT_FOUND(NOT_FOUND, "ID로 해당 리소스를 찾을 수 없습니다."),

    MEMBER_NOT_FOUND(NOT_FOUND, "해당 회원을 찾을 수 없습니다."),

    STORE_NOT_FOUND(NOT_FOUND, "해당 매장을 찾을 수 없습니다."),

    CATEGORY_NOT_FOUND(NOT_FOUND, "해당 카테고리를 찾을 수 없습니다."),

    MENU_NOT_FOUND(NOT_FOUND, "해당 메뉴를 찾을 수 없습니다."),

    WAITING_NOT_FOUND(NOT_FOUND, "해당 웨이팅 정보를 찾을 수 없습니다."),

    /**
     * 500 Internal Server Error
     */
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버에 문제가 발생하였습니다.");

}
