package com.local_dating.matching_service.domain.vo;

import java.time.LocalDate;
import java.time.LocalTime;

public record MatchingScheduleVO(Long matchingId, Long userId, LocalDate matchingDate, LocalTime matchingTime) {
}
