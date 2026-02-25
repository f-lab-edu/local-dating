package com.local_dating.user_service.presentation.controller;

import com.local_dating.user_service.application.UserPricingPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PricingPolicyController {

    private final UserPricingPolicyService userPricingPolicyService;

    @PreAuthorize("isAuthenticated() and #id == principal.userNo")
    @GetMapping(value = "/api/users/{id}/price-policy/{type}")
    public Long viewPricingPolicy(final @PathVariable("id") long id, final @PathVariable("type") String type, final @RequestHeader("Authorization") String authentication) {
        return userPricingPolicyService.viewPricingPolicy(type);
    }
}
