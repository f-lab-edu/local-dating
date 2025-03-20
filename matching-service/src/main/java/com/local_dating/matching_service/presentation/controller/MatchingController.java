package com.local_dating.matching_service.presentation.controller;

import com.local_dating.matching_service.application.MatchingService;
import com.local_dating.matching_service.domain.mapper.MatchingMapper;
import com.local_dating.matching_service.presentation.dto.MatchingDTO;
import com.local_dating.matching_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;
    private final MatchingMapper matchingMapper;
    private final JwtUtil jwtUtil;

    //@PreAuthorize("isAuthenticated() and #id == authentication.getPrincipal()")
    @PostMapping(value = "/v1/users/{id}/matches")
    public void requestMatch(final @PathVariable("id") long id, @RequestHeader("Authorization") String authentication, @RequestBody final MatchingDTO dto) {

        matchingService.requestMatching(id, authentication, matchingMapper.INSTANCE.matchingDTOToMatchingVO(dto));
    }

    @PatchMapping(value = "/v1/users/{id}/matches")
    //@PutMapping(value = "/v1/users/{id}/matches")
    public void updateMatch(@PathVariable("id") long userId, @RequestHeader("Authorization") String authentication, @RequestBody MatchingDTO dto) {
    //public void updateMatch(@PathVariable("id") long userId, @RequestBody MatchingDTO dto) {

        matchingService.updateMatchingInfo(userId, authentication, matchingMapper.INSTANCE.matchingDTOToMatchingVO(dto));
        //matchingService.updateMatchingInfo(userId, matchingMapper.INSTANCE.matchingDTOToMatchingVO(dto));
    }

    @GetMapping(value = "/v1/users/{id}/matches")
    public List<MatchingDTO> getMatch(@PathVariable("id") long userId) {
        return matchingMapper.INSTANCE.matchingVOsToMatchingDTOs(matchingService.getMatchingInfos(userId));
    }

    @GetMapping(value = "/v1/users/{id}/matches/{matchId}")
    public Optional<MatchingDTO> getMatch(@PathVariable("id") long userId, @PathVariable("matchId") long matchId) {
        return Optional.ofNullable(matchingMapper.INSTANCE.matchingVOToMatchingDTO(matchingService.getMatchingInfo(userId, matchId)));
    }

}
