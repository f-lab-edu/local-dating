package com.local_dating.user_service.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MessageCode {
    REGISTER_SUCCESS("REGISTER_SUCCESS", "가입성공", HttpStatus.CREATED)
    , REGISTER_FAIL("REGISTER_FAIL", "가입실패", HttpStatus.INTERNAL_SERVER_ERROR)
    , USER_ALREADY_EXISTS_EXCEPTION("USER_ALREADY_EXISTS_EXCEPTION", "가입되어있음", HttpStatus.CONFLICT)
    , DATA_ALREADY_EXISTS_EXCEPTION("DATA_ALREADY_EXISTS_EXCEPTION", "데이터가있음", HttpStatus.CONFLICT)
    , DATA_NOT_FOUND_EXCEPTION("DATA_NOT_FOUND_EXCEPTION", "데이터가없음", HttpStatus.NOT_FOUND)
    , METHOD_ARGUMENT_NOT_VALID_EXCEPTION("METHOD_ARGUMENT_NOT_VALID_EXCEPTION", "잘못된 데이터입력", HttpStatus.BAD_REQUEST)
    , DATA_INTEGRITY_VIOLATION_EXCEPTION("DATA_INTEGRITY_VIOLATION_EXCEPTION", "데이터 오류", HttpStatus.BAD_REQUEST)
    , BAD_CREDENTIAL_EXCEPTION("BAD_CREDENTIAL_EXCEPTION", "잘못된 회원정보", HttpStatus.UNAUTHORIZED)
    , INVALIDATE_CLAIMS_EXCEPTION("INVALIDATE_CLAIMS_EXCEPTION", "인증 오류", HttpStatus.UNAUTHORIZED)
    , INVALID_VERIFICATION_CODE("INVALID_VERIFICATION_CODE", "인증 코드 오류", HttpStatus.BAD_REQUEST)
    , INSUFFICIENT_COIN("INSUFFICIENT_COIN", "코인 부족", HttpStatus.BAD_REQUEST)
    , SEND_MESSAGE_FAIL("SEND_MESSAGE_FAIL", "메시지 전송 실패", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;

    MessageCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
