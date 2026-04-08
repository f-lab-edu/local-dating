package com.local_dating.matching_service.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MessageCode {
    INVALIDATE_CLAIMS_EXCEPTION("ERROR_CLAIMS", "인증 오류", HttpStatus.UNAUTHORIZED)
    , INSUFFICIENT_COIN("ERROR_COIN", "코인 부족", HttpStatus.BAD_REQUEST)
    , MATCHING_NOT_FOUND("ERROR_MATCHING", "매칭 없음", HttpStatus.NOT_FOUND)
    , MATCHING_SCHEDULE_NOT_FOUND("ERROR_MATCHING_SCHEDULE", "매칭 스케줄 없음", HttpStatus.NOT_FOUND)
    , INSUFFICIENT_AUTHENTICATION("INSUFFICIENT_AUTHENTICATION_EXCEPTION", "인증 만료", HttpStatus.UNAUTHORIZED)
    , BAD_CREDENTIAL_EXCEPTION("BAD_CREDENTIAL_EXCEPTION", "잘못된 회원정보", HttpStatus.UNAUTHORIZED)
    , DATA_ALREADY_EXISTS_EXCEPTION("DATA_ALREADY_EXISTS_EXCEPTION", "데이터가있음", HttpStatus.CONFLICT)
    , EXTERNAL_EXCEPTION("EXTERNAL EXCEPTION", "외부 호출 실패", HttpStatus.LOCKED);

    private final String code;
    private final String message;
    private final HttpStatus status;

    /*MessageCode(String message) {
        this.message = message;
    }*/

    public String getMessage() {
        return message;
    }
}
