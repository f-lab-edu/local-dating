package com.local_dating.user_service.presentation.controller;

import com.local_dating.user_service.application.CustomUserDetails;
import com.local_dating.user_service.application.UserMeetingLocationPreferenceService;
import com.local_dating.user_service.domain.mapper.UserMeetingLocationPreferenceMapper;
import com.local_dating.user_service.presentation.dto.UserMeetingLocationPreferenceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserMeetingLocationPreferenceController {

    private final UserMeetingLocationPreferenceService userMeetingLocationPreferenceService;
    private final UserMeetingLocationPreferenceMapper userMeetingLocationPreferenceMapper;

    @PreAuthorize("isAuthenticated() and #id == principal.userNo")
    @GetMapping(value = "/api/users/{id}/meeting-location-preferences")
    public List<UserMeetingLocationPreferenceDTO> viewPreferences(final @PathVariable("id") Long id, final Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return userMeetingLocationPreferenceMapper.toDtoList(userMeetingLocationPreferenceService.viewPreferences(customUserDetails.getUserNo()));
    }

    @PreAuthorize("isAuthenticated() and #id == principal.userNo")
    @PostMapping(value = "/api/users/{id}/meeting-location-preferences")
    @ResponseStatus(HttpStatus.CREATED)
    public void savePreference(final @PathVariable("id") Long id, final Authentication authentication, final @RequestBody UserMeetingLocationPreferenceDTO userMeetingLocationPreferenceDTO) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        userMeetingLocationPreferenceService.savePreference(customUserDetails.getUserNo(), userMeetingLocationPreferenceMapper.toVo(userMeetingLocationPreferenceDTO));
    }

    @PreAuthorize("isAuthenticated() and #id == principal.userNo")
    @PutMapping(value = "/api/users/{id}/meeting-location-preferences/{preferenceId}")
    public void updatePreference(final @PathVariable("id") Long id, final @PathVariable("preferenceId") Long preferenceId, final Authentication authentication
            , final @RequestBody UserMeetingLocationPreferenceDTO userMeetingLocationPreferenceDTO) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        userMeetingLocationPreferenceService.updatePreference(
                customUserDetails.getUserNo(), preferenceId, userMeetingLocationPreferenceMapper.toVo(userMeetingLocationPreferenceDTO));
    }

}
