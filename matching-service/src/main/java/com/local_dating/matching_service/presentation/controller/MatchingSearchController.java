package com.local_dating.matching_service.presentation.controller;

import com.local_dating.matching_service.application.MatchingSearchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MatchingSearchController {

    private final MatchingSearchService matchingSearchService;

    @GetMapping(value = "/api/matches/users/{id}/next")
    //@GetMapping(value = "/api/matches/next")
    @PreAuthorize("isAuthenticated() and #id == principal.userNo")
    @Operation(summary = "사용자 탐색", description = "사용자를 탐색한다")
    public String searchNext(final @PathVariable("id") Long id, @RequestHeader("Authorization") final String authorization) {
    //public String searchNext(@AuthenticationPrincipal(expression = "userNo") Long userNo, String authentication) {
        return matchingSearchService.searchNext(id, authorization);
        //return matchingSearchService.searchNext(id, authentication);
    }
}
