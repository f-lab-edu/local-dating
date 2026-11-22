package com.local_dating.matching_service.domain.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OperationStatus {
    PENDING("PENDING", "대기")
    , SUCCESS("SUCCESS", "성공")
    , FAILURE("FAILURE", "실패");

    private final String code;
    private final String message;
}
