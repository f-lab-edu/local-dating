package com.local_dating.matching_service.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchingTimeType {

    AFTERNOON("AFTERNOON", "점심")
    , EVENING("EVENING", "저녁");

    private final String code;
    private final String message;
}
