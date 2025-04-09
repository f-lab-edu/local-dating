package com.local_dating.matching_service.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record MatchingScheduleDTO(Long matchingId, Long userId, @JsonFormat(pattern = "yyyyMMdd") LocalDate matchingDate, @JsonFormat(pattern = "HHmm") LocalTime matchingTime) {
}
