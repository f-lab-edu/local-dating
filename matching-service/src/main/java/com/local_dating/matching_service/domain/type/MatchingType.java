package com.local_dating.matching_service.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchingType {





    MATCHING_REQUEST("MATCHING_REQUEST", "매칭 요청")

    , NEW("NEW", "신규매칭") // 정리 필요
    , MATCHED("MATCHED", "매칭") // 정리 필요
    , SAME("SAME", "스케줄 일치") // 정리 필요
    , REQUEST("REQUEST", "스케줄 요청") // 정리 필요


    , MATCHING_ACCEPT	("MATCHING_ACCEPT", "매칭 요청 수락")
    , SCHEDULE_ACCEPT("SCHEDULE_ACCEPT", "스케줄 확정")
    , MEETING_PENDING("MEETING_PENDING", "만남일 위치정보 갱신 대기")
    , MEETING_COMPLETE("MEETING_COMPLETE", "만남 완료")
    , MEETING_FAIL("MEETING_FAIL", "만남 불성립")
    , REVIEW_MATCHED("REVIEW_MATCHED", "양측 좋아요 후기")
    , END("END", "종료");

    private final String code;
    private final String message;
}
