package com.local_dating.matching_service.presentation.controller;

import com.local_dating.matching_service.application.MatchingScheduleService;
import com.local_dating.matching_service.domain.mapper.MatchingScheduleMapper;
import com.local_dating.matching_service.presentation.dto.MatchingScheduleDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "MatchingSchedule API", description = "매칭 스케줄 조정 API")
public class MatchingScheduleController {

    private final MatchingScheduleService matchingScheduleService;
    private final MatchingScheduleMapper matchingScheduleMapper;

    @GetMapping(value = "/v1/matches/{id}/users/{userId}/schedule")
    @Operation(summary = "매칭 스케줄 조회", description = "특정 매칭에 대해서 들어간 일정 확인")
    public List<MatchingScheduleDTO> viewMatchingSchedule(@RequestHeader("Authorization") String authentication, @PathVariable Long id, @PathVariable Long userId) {
        return matchingScheduleMapper.matchingScheduleVOsToMatchingScheduleDTOs(matchingScheduleService.viewSchedule(id, userId));
    }

    @PostMapping(value = "/v1/matches/{id}/users/{userId}/schedules")
    public void saveMatchingSchedule(@PathVariable final Long id, @PathVariable("userId") final Long userId
            , @RequestHeader("Authorization") String authentication
            , @RequestBody final List<MatchingScheduleDTO> matchingScheduleDTOs) {
        matchingScheduleService.saveSchedule(id, userId, matchingScheduleMapper.matchingScheduleDTOsToMatchingScheduleVOs(matchingScheduleDTOs));
    }

    @PutMapping(value = "/v1/matches/{id}/users/{userId}/schedules")
    public void updateMatchingSchedule(@PathVariable final Long id, @PathVariable("userId") final Long userId
            , @RequestHeader("Authorization") String authentication
            , @RequestBody final MatchingScheduleDTO matchingScheduleDTO) {
            //, @RequestBody final List<MatchingScheduleDTO> matchingScheduleDTOs) {
        matchingScheduleService.updateSchedule(id, userId, matchingScheduleMapper.matchingScheduleDTOToMatchingScheduleVO(matchingScheduleDTO));
    }
}
