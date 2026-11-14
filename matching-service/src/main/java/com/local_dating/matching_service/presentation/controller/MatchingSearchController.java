package com.local_dating.matching_service.presentation.controller;

import com.local_dating.matching_service.application.MatchingSearchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MatchingSearchController {
    private final MatchingSearchService matchingSearchService;

    @PostMapping("/api/matches/users/{id}/next")
    @PreAuthorize("isAuthenticated() and #id == principal.userNo")
    @Operation(summary = "다음 사용자 카드 탐색", description = "사용자를 탐색한다")
    public String searchNext(@PathVariable("id") final Long id, @RequestHeader("Authorization") final String authorization) {
        return matchingSearchService.searchNext(id, authorization);
        //String card = matchingSearchService.searchNext(id, authorization);
        //return card == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(card);
    }

    @GetMapping("/api/matches/users/{id}/current")
    @PreAuthorize("isAuthenticated() and #id == principal.userNo")
    @Operation(summary = "현재 보는 카드 조회", description = "현재 카드만 조회")
    public String searchCurrent(@PathVariable("id") final Long id, @RequestHeader("Authorization") final String authorization) {
        return matchingSearchService.searchCurrent(id, authorization);
        //String card = matchingSearchService.searchCurrent(id, authorization);
        //return card == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(card);
    }
}
