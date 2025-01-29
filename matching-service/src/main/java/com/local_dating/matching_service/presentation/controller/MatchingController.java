package com.local_dating.matching_service.presentation.controller;

import com.local_dating.matching_service.application.MatchingService;
import com.local_dating.matching_service.domain.mapper.MatchingMapper;
import com.local_dating.matching_service.presentation.dto.MatchingDTO;
import com.local_dating.matching_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;
    private final MatchingMapper matchingMapper;
    private final JwtUtil jwtUtil;

    //@PreAuthorize("isAuthenticated() and #id == authentication.getPrincipal()")
    @PostMapping(value = "/v1/users/{id}/matches")
    public void requestMatch(final @PathVariable("id") long id, @RequestHeader("Authorization") String authentication, @RequestBody final MatchingDTO dto) {
    //public void requestMatch(final @PathVariable("id") long userid, final Authentication authentication, @RequestBody final MatchingDTO dto) {

        //String userId = jwtUtil.getAuthenticationFromToken(authentication).getPrincipal().toString();

        matchingService.requestMatching(id, authentication, matchingMapper.INSTANCE.matchingDTOToMatchingVO(dto));
    }

    @PutMapping(value = "/v1/users/{id}/matches")
    public void updateMatch(@PathVariable("id") long userid, @RequestBody MatchingDTO dto) {

        matchingService.updateMatchingInfo(userid, matchingMapper.INSTANCE.matchingDTOToMatchingVO(dto));
    }
}
