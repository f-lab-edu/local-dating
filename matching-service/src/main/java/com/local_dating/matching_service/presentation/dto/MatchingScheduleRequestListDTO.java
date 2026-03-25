package com.local_dating.matching_service.presentation.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MatchingScheduleRequestListDTO {

    private List<MatchingScheduleRequestDTO> matchingScheduleRequestDTOs;
}
