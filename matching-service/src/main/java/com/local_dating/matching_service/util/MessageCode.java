package com.local_dating.matching_service.util;

public enum MessageCode {
    INVALIDATE_CLAIMS_EXCEPTION("인증 오류");

    private final String message;

    MessageCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
