package com.local_dating.matching_service.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MessageCode {
    INVALIDATE_CLAIMS_EXCEPTION("ERROR_CLAIMS", "인증 오류", HttpStatus.BAD_REQUEST)
    , INSUFFICIENT_COIN("ERROR_COIN", "코인 부족", HttpStatus.BAD_REQUEST);
    //INVALIDATE_CLAIMS_EXCEPTION("인증 오류");

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
