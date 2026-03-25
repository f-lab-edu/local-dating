package com.local_dating.matching_service.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchingScheduleRoundType {

    OPEN("OPEN", "스케줄 제출 중")
    , SUBMITTED("SUBMITTED", "스케줄 제출 완료")
    , CONFIRMED("CONFIRMED", "확정")
    , NOT_CONFIRMED("NOT_CONFIRMED", "미확정")
    , CANCELLED("CANCELLED", "취소")
    , EXPIRED("EXPIRED", "기한 만료");

    private final String code;
    private final String message;
}
