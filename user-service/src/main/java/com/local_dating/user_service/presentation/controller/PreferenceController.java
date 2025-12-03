package com.local_dating.user_service.presentation.controller;

import com.local_dating.user_service.application.CustomUserDetails;
import com.local_dating.user_service.application.UserPreferenceService;
import com.local_dating.user_service.domain.mapper.UserPreferenceMapper;
import com.local_dating.user_service.presentation.dto.UserPreferenceDTO;
import com.local_dating.user_service.presentation.dto.UserPreferenceListDTO;
import com.local_dating.user_service.util.exception.DataNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.local_dating.user_service.util.MessageCode.DATA_NOT_FOUND_EXCEPTION;

@RestController
@RequiredArgsConstructor
public class PreferenceController {

    private final UserPreferenceMapper userPreferenceMapper;
    private final UserPreferenceService userPreferenceService;

    @PreAuthorize("isAuthenticated() and #id == principal.userNo")
    @PostMapping(value = "/v1/users/{id}/preference")
    public void savePreference(final @PathVariable("id") long id, final Authentication authentication, @RequestBody @Valid final UserPreferenceListDTO userPreferenceDTOList) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        userPreferenceService.savePreferences(customUserDetails.getUserNo(), UserPreferenceMapper.INSTANCE.toUserPreferenceVOList(userPreferenceDTOList.getUserPreferences()));
    }

    @PreAuthorize("isAuthenticated() and #id == principal.userNo")
    @PutMapping(value = "/v1/users/{id}/preference")
    public void updatePreference(final @PathVariable("id") long id, final Authentication authentication, @RequestBody @Valid final UserPreferenceListDTO userPreferenceDTOList) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        userPreferenceService.updatePreferences(customUserDetails.getUserNo(), userPreferenceMapper.INSTANCE.toUserPreferenceVOList(userPreferenceDTOList.getUserPreferences()));
    }

    @PreAuthorize("isAuthenticated() and #id == principal.userNo")
    @GetMapping(value = "/v1/users/{id}/preference")
    public String viewPreference(final @PathVariable("id") long id, final Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String result = userPreferenceService.viewPreference(customUserDetails.getUserNo());
        if (result.isEmpty()) {
            throw new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION.getMessage());
        }
        return result;
    }

    @PreAuthorize("isAuthenticated() and #id == principal.userNo")
    @PatchMapping(value = "/v1/users/{id}/preference/prior")
    public void updatePreferencePriority(final @PathVariable("id") long id, final Authentication authentication, @RequestBody final List<UserPreferenceDTO> userPreferenceDTOList) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        userPreferenceService.updatePreferencesPriority(customUserDetails.getUserNo(), userPreferenceMapper.INSTANCE.toUserPreferenceVOList(userPreferenceDTOList));
    }
}
