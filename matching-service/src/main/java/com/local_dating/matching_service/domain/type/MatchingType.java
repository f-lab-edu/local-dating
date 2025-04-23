package com.local_dating.matching_service.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchingType {

    MATCHED("MATCHED", "매칭")
    , END("END", "종료")
    , SAME("SAME", "스케줄 일치")
    , REQUEST("REQUEST", "스케줄 요청");

    private final String code;
    private final String message;
}
