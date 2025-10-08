package com.local_dating.user_service.presentation.controller;

import com.local_dating.user_service.application.UserProfileService;
import com.local_dating.user_service.domain.mapper.UserProfileMapper;
import com.local_dating.user_service.presentation.dto.UserProfileListDTO;
import com.local_dating.user_service.util.exception.DataNotFoundException;
import jakarta.validation.Valid;
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

    @Secured("ROLE_USER")
    //@PreAuthorize("isAuthenticated() and #id == authentication.getPrincipal()")
    @PostMapping(value = "/v1/users/{id}/profile")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveProfile(final @PathVariable("id") long id, final Authentication authentication, @RequestBody @Valid final UserProfileListDTO userProfileDTOList) {
    //public void saveProfile(final @PathVariable("id") long id, final Authentication authentication, @RequestBody @Valid final UserProfileListDTO userProfileDTOList) {

        Long userId = (Long) authentication.getPrincipal();
        userProfileService.saveProfile(userId, userProfileMapper.INSTANCE.toUserProfileVOList(userProfileDTOList.getUserProfiles()));
    }

    @PreAuthorize("hasRole('USER') and isAuthenticated() and #id == authentication.getPrincipal()")
    @PatchMapping(value = "/v1/users/{id}/profile")
    public void updateProfile(final @PathVariable("id") long id, final Authentication authentication, @RequestBody @Valid final UserProfileListDTO userProfileDTOList) {
        Long userId = (Long) authentication.getPrincipal();
        userProfileService.updateProfile(userId, userProfileMapper.INSTANCE.toUserProfileVOList(userProfileDTOList.getUserProfiles()));
    }

    @Secured("ROLE_USER")
    //@PreAuthorize("isAuthenticated() and #id == authentication.getPrincipal()")
    @GetMapping(value = "/v1/users/{id}/profile")
    public List viewProfile(final @PathVariable("id") long id, final Authentication authentication) {

        return Optional.of(userProfileService.viewProfile((Long) authentication.getPrincipal()))
                .map(list -> UserProfileMapper.INSTANCE.toUserProfileDTOList(list))
                .orElseThrow(() -> new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION.getMessage()));
    }
}
