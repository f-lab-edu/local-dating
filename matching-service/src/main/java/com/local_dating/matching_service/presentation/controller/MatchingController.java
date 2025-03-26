package com.local_dating.matching_service.presentation.controller;

import com.local_dating.matching_service.application.MatchingService;
import com.local_dating.matching_service.domain.mapper.MatchingMapper;
import com.local_dating.matching_service.presentation.dto.MatchingDTO;
import com.local_dating.matching_service.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "Matching API", description = "매칭 관리 API")
public class MatchingController {

    private final MatchingService matchingService;
    private final MatchingMapper matchingMapper;
    private final JwtUtil jwtUtil;

    //@PreAuthorize("isAuthenticated() and #id == authentication.getPrincipal()")
    @PostMapping(value = "/v1/users/{id}/matches")
    @Operation(summary = "매칭 생성", description = "매칭을 생성한다")
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

    @GetMapping(value = "/v1/users/{id}/matches") //기본 api
    public List<MatchingDTO> getMatch(@PathVariable("id") long userId) {
        return matchingMapper.INSTANCE.matchingVOsToMatchingDTOs(matchingService.getMatchingInfos(userId));
    }

    @GetMapping(value = "/v1/users/{id}/matches/{matchId}") //기본 api
    public Optional<MatchingDTO> getMatch(@PathVariable("id") long userId, @PathVariable("matchId") long matchId) {
        return Optional.ofNullable(matchingMapper.INSTANCE.matchingVOToMatchingDTO(matchingService.getMatchingInfo(userId, matchId)));
    }

    @GetMapping(value = "/v1/users/{id}/received-matches")
    //@PreAuthorize("isAuthenticated() and #id == authentication.getPrincipal()")
    @Operation(summary = "매칭 조회", description = "받은 매칭")
    public List<MatchingDTO> getReceivedMatches(@PathVariable("id") final long userId) {
        //String userId = authentication.getPrincipal().toString();
        return matchingMapper.INSTANCE.matchingVOsToMatchingDTOs(matchingService.getReceivedMatches(userId));
    }

    @GetMapping(value = "/v1/users/{id}/sent-matches")
    //@PreAuthorize("isAuthenticated() and #id == authentication.getPrincipal()")
    @Operation(summary = "매칭 조회", description = "보낸 매칭")
    public List<MatchingDTO> getSentMatches(@PathVariable("id") final long userId, final Authentication authentication) {
        return matchingMapper.INSTANCE.matchingVOsToMatchingDTOs(matchingService.getSentMatches(userId));
    }

    @PutMapping(value = "/v1/users/{id}/accept-matches")
    @Operation(summary = "매칭 수락", description = "보낸 매칭에 대한 수락, 코인 지불")
    public void acceptMatch(@PathVariable("id") final long userId, @RequestHeader("Authorization") String authentication, @RequestBody final MatchingDTO dto) {
        matchingService.AcceptMatching(userId, authentication, matchingMapper.INSTANCE.matchingDTOToMatchingVO(dto));
    }

    @PutMapping(value = "/v1/users/{id}/reject-matches")
    @Operation(summary = "매칭 거절", description = "보낸 매칭에 대한 거절, 요청자 코인 환불")
    public void rejectMatch(@PathVariable("id") final long userId, @RequestHeader("Authorization") String authentication, @RequestBody final MatchingDTO dto) {
        matchingService.rejectMatching(userId, authentication, matchingMapper.INSTANCE.matchingDTOToMatchingVO(dto));
    }
}
