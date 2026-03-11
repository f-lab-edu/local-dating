package com.local_dating.matching_service.presentation.dto;

import java.time.LocalDateTime;

public record MatchingDTO(
        Long id, Long requId, Long recvId
        , String statusCd
        , LocalDateTime decisionStartDate
        , LocalDateTime decisionEndDate
        , String requStatusCd, String recvStatusCd, String matchPlace, String matchDate, String matchTime
) {
}
