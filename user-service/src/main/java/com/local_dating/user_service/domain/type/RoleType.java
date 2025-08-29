package com.local_dating.user_service.domain.type;

public enum RoleType {

    USER("ROLE_USER", "일반 사용자")
    , ADMIN("ROLE_ADMIN", "관리자");

    private final String code;
    private final String message;

    RoleType(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
