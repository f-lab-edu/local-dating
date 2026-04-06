package com.local_dating.matching_service.presentation.dto;

import java.time.LocalDate;

public record MatchingScheduleRequestDTO(Long id, Long matchingId, Long matchingScheduleRoundId, Long userId
        , LocalDate matchingDate, String matchingTimeType, String statusCd) {
}
