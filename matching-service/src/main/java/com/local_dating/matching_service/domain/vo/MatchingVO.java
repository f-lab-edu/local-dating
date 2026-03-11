package com.local_dating.matching_service.domain.vo;

import java.time.LocalDateTime;

public record MatchingVO(
        Long id, Long requId, Long recvId
        , String statusCd
        , LocalDateTime decisionStartDate
        , LocalDateTime decisionEndDate
        , String requStatusCd, String recvStatusCd
        , String matchPlace, String matchDate, String matchTime
) {
}
