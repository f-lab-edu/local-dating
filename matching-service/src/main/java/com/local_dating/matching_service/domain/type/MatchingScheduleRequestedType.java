package com.local_dating.matching_service.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchingScheduleRequestedType {

    //AFTERNOON("AFTERNOON", "낮") // matchingTimeType 코드 분리
    //, EVENING("EVENING", "저녁")

    SUBMITTED("SUBMITTED", "스케줄 제출 완료") // statusCd 코드
    , CONFIRMED("CONFIRMED", "확정")
    , CANCELLED("CANCELLED", "취소")
    , EXPIRED("EXPIRED", "기한 만료");

    private final String code;
    private final String message;
}
