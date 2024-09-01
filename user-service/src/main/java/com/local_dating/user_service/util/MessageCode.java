package com.local_dating.user_service.util;

public enum MessageCode {
    REGISTER_SUCCESS("가입성공")
    , REGISTER_FAIL("가입실패")
    , USER_ALREADY_EXISTS("가입되어있음")
    , METHOD_ARGUMENT_NOT_VALID("잘못된 데이터입력")
    , DATA_INTEGRITY_VIOLATION("데이터 오류")
    , BAD_CREDENTIAL("잘못된 회원정보");

    private final String message;

    MessageCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
