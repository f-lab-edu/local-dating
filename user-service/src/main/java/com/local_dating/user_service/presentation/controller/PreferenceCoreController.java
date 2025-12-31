package com.local_dating.user_service.presentation.controller;

import com.local_dating.user_service.application.CustomUserDetails;
import com.local_dating.user_service.application.UserPreferenceCoreService;
import com.local_dating.user_service.domain.mapper.UserPreferenceCoreMapper;
import com.local_dating.user_service.domain.vo.UserProfileCoreVO;
import com.local_dating.user_service.presentation.dto.UserPreferenceCoreDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PreferenceCoreController {

    private final UserPreferenceCoreService userPreferenceCoreService;
    private final UserPreferenceCoreMapper userPreferenceCoreMapper;

    @Secured(value = {"ROLE_USER"})
    @PreAuthorize("isAuthenticated() and #id == principal.userNo")
    @GetMapping(value = "/api/users/{id}/preference-core")
    public UserPreferenceCoreDTO viewPreferenceCore(final @PathVariable("id") Long id, final Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return userPreferenceCoreMapper.userPreferenceVoToUserPreferenceCoreDto(userPreferenceCoreService.viewPreferenceCore(customUserDetails.getUserNo()));
    }

    @Secured(value = {"ROLE_USER"})
    @PreAuthorize("isAuthenticated() and #id == principal.userNo")
    @PostMapping(value = "/api/users/{id}/preference-core")
    public void savePreferenceCore(final @PathVariable("id") Long id, final Authentication authentication, @RequestBody final UserPreferenceCoreDTO userPreferenceCoreDTO) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        userPreferenceCoreService.savePreferenceCore(userPreferenceCoreMapper.UserPreferenceCoreDtoToUserPreferenceVo(userPreferenceCoreDTO));
    }

    @Secured(value = {"ROLE_USER"})
    @PreAuthorize("isAuthenticated() and #id == principal.userNo")
    @GetMapping(value = "/api/users/{id}/next")
    public List<UserProfileCoreVO> searchNext(final @PathVariable("id") Long id, final Authentication authentication) {
    //public List<UserProfileCoreVO> searchNext(final @PathVariable("id") Long id) {
        return userPreferenceCoreService.searchNext(id);
    }

    /*@GetMapping(value = "/api/users/{id}/next")
    public List<UserProfileCoreVO> searchNext(final @PathVariable("id") Long id) {
        return userPreferenceCoreService.searchNext(id);
    }*/
}
