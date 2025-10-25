package com.local_dating.user_service.presentation.controller;

import com.local_dating.user_service.application.UserProfileCoreService;
import com.local_dating.user_service.domain.mapper.UserProfileCoreMapper;
import com.local_dating.user_service.presentation.dto.UserProfileCoreDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileCoreController {

    private final UserProfileCoreService saveProfileCoreService;
    private final UserProfileCoreMapper userProfileMapper;

    @PostMapping(value = "/api/users/{id}/profile-core")
    @PreAuthorize("hasRole('USER') and isAuthenticated() and #id == authentication.getPrincipal()")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveProfile(final @PathVariable("id") long id, final Authentication authentication , @RequestBody final UserProfileCoreDTO userProfileCoreDTO) {
        saveProfileCoreService.saveProfileCore(userProfileMapper.INSTANCE.toUserProfileCoreVO(userProfileCoreDTO));
    }
}
