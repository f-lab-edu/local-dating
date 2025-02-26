package com.local_dating.user_service.util;

public enum MessageCode {
    REGISTER_SUCCESS("가입성공")
    , REGISTER_FAIL("가입실패")
    , USER_ALREADY_EXISTS_EXCEPTION("가입되어있음")
    , DATA_ALREADY_EXISTS_EXCEPTION("데이터가있음")
    , DATA_NOT_FOUND_EXCEPTION("데이터가없음")
    , METHOD_ARGUMENT_NOT_VALID_EXCEPTION("잘못된 데이터입력")
    , DATA_INTEGRITY_VIOLATION_EXCEPTION("데이터 오류")
    , BAD_CREDENTIAL_EXCEPTION("잘못된 회원정보")
    , INVALIDATE_CLAIMS_EXCEPTION("인증 오류")
    , INSUFFICIENT_COIN("코인 부족");

    private final String message;

    MessageCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
