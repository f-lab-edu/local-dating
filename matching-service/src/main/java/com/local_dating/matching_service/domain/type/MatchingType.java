package com.local_dating.matching_service.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchingType {

    MATCHED("MATCHED", "매칭")
    , END("END", "종료");

    private final String code;
    private final String message;
}
