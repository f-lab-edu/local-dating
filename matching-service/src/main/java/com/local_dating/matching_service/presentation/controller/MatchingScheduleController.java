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

    @GetMapping(value = "/v1/matches/{id}/users/{userId}/schedules")
    @Operation(summary = "매칭 스케줄 조회", description = "특정 매칭에 대해서 들어간 일정 확인")
    public List<MatchingScheduleDTO> viewMatchingSchedule(@RequestHeader("Authorization") String authentication, @PathVariable Long id, @PathVariable Long userId) {
        return matchingScheduleMapper.matchingScheduleVOsToMatchingScheduleDTOs(matchingScheduleService.viewSchedule(id, userId));
    }

    @GetMapping(value = "/v1/matches/{id}/users/{userId}/schedules/same")
    @Operation(summary = "매칭 스케줄 조회", description = "특정 매칭에 대해서 공통 스케줄 확인")
    public List<MatchingScheduleDTO> viewMatchingScheduleSame(@RequestHeader("Authorization") String authentication, @PathVariable Long id, @PathVariable Long userId) {
        return matchingScheduleMapper.matchingScheduleVOsToMatchingScheduleDTOs(matchingScheduleService.viewScheduleSame(id, userId));
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

    @PutMapping(value = "/v1/matches/{id}/users/{userId}/accept-schedules/{scheduleId}")
    public void requestMatchSchedule(@PathVariable final Long id, @PathVariable("scheduleId") final Long scheduleId, @RequestHeader("Authorization") String authentication) {
        matchingScheduleService.requestMatchingSchedule(id, scheduleId);
    }

    @PutMapping(value = "/v1/matches/{id}/users/{userId}/accept-schedules/{scheduleId}")
    public void acceptMatchSchedule(@PathVariable final Long id, @PathVariable("scheduleId") final Long scheduleId, @RequestHeader("Authorization") String authentication) {
        matchingScheduleService.acceptMatchingSchedule(id, scheduleId);
    }

    @PutMapping(value = "/v1/matches/{id}/users/{userId}/reject-schedules/{scheduleId}")
    public void rejectMatchSchedule(@PathVariable final Long id, @PathVariable("scheduleId") final Long scheduleId, @RequestHeader("Authorization") String authentication) {
        matchingScheduleService.rejectMatchingSchedule(id, scheduleId);
    }

}
