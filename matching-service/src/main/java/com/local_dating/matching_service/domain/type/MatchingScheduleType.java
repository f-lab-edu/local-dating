package com.local_dating.matching_service.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchingScheduleType {

    /*NEW("NEW", "신규매칭")
    , MATCHED("MATCHED", "매칭")
    , END("END", "종료")
    , SAME("SAME", "스케줄 일치")
    , REQUEST("REQUEST", "스케줄 요청")*/

    SUBMITTED("SUBMITTED", "신규 날짜 등록")
    , SELECTED("SELECT", "선택한 날짜")
    , CONFIRMED("CONFIRMED", "확정된 스케줄")
    , REJECTED("REJECTED", "제외된 날짜");

    private final String code;
    private final String message;
}
