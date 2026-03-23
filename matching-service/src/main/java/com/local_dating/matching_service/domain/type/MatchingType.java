package com.local_dating.matching_service.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchingType {





    MATCHING_REQUEST("MATCHING_REQUEST", "매칭 요청", "-")

    , NEW("NEW", "신규매칭", null) // 정리 필요
    , MATCHED("MATCHED", "매칭", null) // 정리 필요
    , SAME("SAME", "스케줄 일치", null) // 정리 필요
    , REQUEST("REQUEST", "스케줄 요청", null) // 정리 필요


    , MATCHING_ACCEPT	("MATCHING_ACCEPT", "매칭 요청 수락", "-")
    , MATCHING_REJECT	("MATCHING_REJECT", "매칭 요청 거절", "+")
    , SCHEDULE_ACCEPT("SCHEDULE_ACCEPT", "스케줄 확정", null)
    , MEETING_PENDING("MEETING_PENDING", "만남일 위치정보 갱신 대기", null)
    , MEETING_COMPLETE("MEETING_COMPLETE", "만남 완료", null)
    , MEETING_FAIL("MEETING_FAIL", "만남 불성립", null)
    , REVIEW_MATCHED("REVIEW_MATCHED", "양측 좋아요 후기", null)
    , END("END", "종료", null);

    private final String code;
    private final String message;
    private final String operator;
}
