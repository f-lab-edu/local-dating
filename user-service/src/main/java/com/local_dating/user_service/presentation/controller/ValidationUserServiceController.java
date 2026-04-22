package com.local_dating.user_service.presentation.controller;

import com.local_dating.user_service.application.ValidateUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ValidationUserServiceController {

    private final ValidateUserService validateUserService;

    @GetMapping("/api/validate/users/{id}")
    public void validateUserId(final @PathVariable("id") Long userId, final Authentication authentication) {
        validateUserService.validateUserId(userId);
    }
}
