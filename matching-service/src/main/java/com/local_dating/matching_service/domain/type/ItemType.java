package com.local_dating.matching_service.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemType {
    SEARCHING("SEARCHING", "검색")
    , MATCHING("MATCHING", "매칭");

    private final String code;
    private final String message;
}
