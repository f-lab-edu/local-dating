package com.local_dating.user_service.presentation.controller;

import com.local_dating.user_service.application.UserProfileCoreService;
import com.local_dating.user_service.domain.mapper.UserProfileCoreMapper;
import com.local_dating.user_service.presentation.dto.UserProfileCoreDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProfileCoreController {

    private final UserProfileCoreService userProfileCoreService;
    private final UserProfileCoreMapper userProfileCoreMapper;

    @PreAuthorize("isAuthenticated() and #id == principal.userNo")
    @GetMapping(value = "/api/users/{id}/profile-core")
    public UserProfileCoreDTO viewProfile(final @PathVariable("id") long id) {
        return userProfileCoreMapper.userProfileVoToUserProfileCoreDto(userProfileCoreService.viewProfileCore(id));
        //CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        //return userProfileCoreMapper.userProfileVoToUserProfileCoreDto(userProfileCoreService.viewProfileCore(customUserDetails.getUserNo()));
    }

    @PostMapping(value = "/api/users/{id}/profile-core")
    @PreAuthorize("hasRole('USER') and isAuthenticated() and #id == principal.userNo")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveProfile(final @PathVariable("id") long id, @RequestBody final UserProfileCoreDTO userProfileCoreDTO) {
        userProfileCoreService.saveProfileCore(id, userProfileCoreMapper.INSTANCE.toUserProfileCoreVo(userProfileCoreDTO));
    }
}
