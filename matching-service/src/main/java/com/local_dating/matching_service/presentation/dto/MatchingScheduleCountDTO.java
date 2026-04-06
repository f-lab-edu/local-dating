package com.local_dating.matching_service.presentation.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MatchingScheduleCountDTO {

    private LocalDate date;
    private Long userCount;

}
