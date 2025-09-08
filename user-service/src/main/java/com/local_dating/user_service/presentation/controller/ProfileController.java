package com.local_dating.user_service.presentation.controller;

import com.local_dating.user_service.application.UserProfileService;
import com.local_dating.user_service.domain.mapper.UserProfileMapper;
import com.local_dating.user_service.presentation.dto.UserProfileDTO;
import com.local_dating.user_service.util.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.local_dating.user_service.util.MessageCode.DATA_NOT_FOUND_EXCEPTION;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    private final UserProfileService userProfileService;
    private final UserProfileMapper userProfileMapper;

    @Secured("ROLE_ADMIN")
    //@PreAuthorize("isAuthenticated() and #id == authentication.getPrincipal()")
    @PostMapping(value = "/v1/users/{id}/profile")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveProfile(final @PathVariable("id") long id, final Authentication authentication, @RequestBody final List<UserProfileDTO> userProfileDTO) {

        String userId = authentication.getPrincipal().toString();
        userProfileService.saveProfile(userId, userProfileMapper.INSTANCE.toUserProfileVOList(userProfileDTO));
    }

    @PreAuthorize("hasRole('USER') and isAuthenticated() and #id == authentication.getPrincipal()")
    //@PreAuthorize("hasRole('ROLE_USER') and isAuthenticated() and #id == authentication.getPrincipal()")
    @PatchMapping(value = "/v1/users/{id}/profile")
    public void updateProfile(final @PathVariable("id") long id, final Authentication authentication, @RequestBody final List<UserProfileDTO> userProfileDTOList) {
        String userId = authentication.getPrincipal().toString();
        userProfileService.updateProfile(userId, userProfileMapper.INSTANCE.toUserProfileVOList(userProfileDTOList));
    }

    @Secured("USER")
    //@PreAuthorize("isAuthenticated() and #id == authentication.getPrincipal()")
    @GetMapping(value = "/v1/users/{id}/profile")
    public List viewProfile(final @PathVariable("id") long id, final Authentication authentication) {

        return Optional.of(userProfileService.viewProfile(authentication.getPrincipal().toString()))
                .map(list -> UserProfileMapper.INSTANCE.toUserProfileDTOList(list))
                .orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION.getMessage()));
    }
}
