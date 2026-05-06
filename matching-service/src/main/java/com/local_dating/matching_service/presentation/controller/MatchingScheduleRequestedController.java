package com.local_dating.matching_service.presentation.controller;

import com.local_dating.matching_service.application.MatchingScheduleRequestedService;
import com.local_dating.matching_service.application.validation.MatchingScheduleRequestValidator;
import com.local_dating.matching_service.domain.mapper.MatchingScheduleRequestedMapper;
import com.local_dating.matching_service.presentation.dto.MatchingScheduleRequestDTO;
import com.local_dating.matching_service.presentation.dto.MatchingScheduleRequestListDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MatchingScheduleRequestedController {

    private final MatchingScheduleRequestedService matchingScheduleRequestedService;
    private final MatchingScheduleRequestedMapper matchingScheduleRequestedMapper;
    private final MatchingScheduleRequestValidator matchingScheduleRequestValidator;

    @GetMapping(value = "/api/matches/{match}/users/{id}/round/{round}")
    @PreAuthorize("isAuthenticated() and #id.toString() == principal")
    @Operation(summary = "라운드에 해당하는 매칭 스케줄 조회", description = "라운드에 해당하는 매칭 스케줄 조회")
    public List<MatchingScheduleRequestDTO> getMatchingScheduleRequested(final @PathVariable("match") Long match, final @PathVariable("id") Long id, final @PathVariable("round") Long round, @RequestHeader("Authorization") final String authorization) {
    //public List<MatchingScheduleRequested> getMatchingScheduleRequested(final @PathVariable("match") Long match, final @PathVariable("id") Long id, final @PathVariable("round") Long round, @RequestHeader("Authorization") final String authorization) {
        return matchingScheduleRequestedMapper.entityListToDtoList(matchingScheduleRequestedService.getMatchingScheduleRequested(match, id, round, authorization));
    }
    /*public List<MatchingScheduleRequested> getMatchingScheduleRequested(final @PathVariable("match") Long match, final @PathVariable("id") Long id, final @PathVariable("round") Long round, @RequestHeader("Authorization") final String authorization) {
        return matchingScheduleRequestedService.getMatchingScheduleRequested(match, id, round, authorization);
    }*/

    @PostMapping(value = "/api/matches/users/{id}/schedule-requests")
    @PreAuthorize("isAuthenticated() and #id.toString() == principal")
    @Operation(summary = "라운드에 해당하는 매칭 스케줄 등록", description = "라운드에 해당하는 매칭 스케줄 등록, 스케줄계산")
    public void saveMatchingScheduleRequested(final @PathVariable("id") Long id, @RequestHeader("Authorization") final String authorization, @RequestBody final MatchingScheduleRequestListDTO matchingScheduleRequestListDTO) {

        //matchingScheduleRequestValidator.validationMatchingScheduleList(id, matchingScheduleRequestListDTO);

        matchingScheduleRequestedService.saveMatchingScheduleRequested(id, authorization, matchingScheduleRequestListDTO.getMatchingScheduleRequestDTOs());
        matchingScheduleRequestedService.checkRoundSchedule(matchingScheduleRequestListDTO.getMatchingScheduleRequestDTOs().get(0));
        //matchingScheduleRequestedService.checkRoundSchedule(id, authorization, matchingScheduleRequestListDTO.getMatchingScheduleRequestDTOs());
        //return matchingScheduleRequestedService.saveMatchingScheduleRequested(id, authorization, matchingScheduleRequestDTO);
    }

    //@Operation(summary = "스케줄계산", description = "라운드에 양 사용자 스케줄 등록 확인, 스케줄계산")

}
