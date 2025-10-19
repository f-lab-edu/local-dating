package com.local_dating.user_service.presentation.controller;

import com.local_dating.user_service.application.UserPreferenceService;
import com.local_dating.user_service.domain.entity.UserPreference;
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

    @PreAuthorize("isAuthenticated() and #id == authentication.getPrincipal()")
    @PostMapping(value = "/v1/users/{id}/preference")
    public List savePreference(final @PathVariable("id") long id, final Authentication authentication, @RequestBody @Valid final UserPreferenceListDTO userPreferenceDTOList) {
        List<UserPreference> userPreferenceList = userPreferenceService.savePreferences((Long) authentication.getPrincipal(), UserPreferenceMapper.INSTANCE.toUserPreferenceVOList(userPreferenceDTOList.getUserPreferences()));
        //List<UserPreference> userPreferenceList = userPreferenceService.savePreferences(authentication.getPrincipal().toString(), UserPreferenceMapper.INSTANCE.toUserPreferenceVOList(userPreferenceDTOList));
        return userPreferenceList;
    }

    @PreAuthorize("isAuthenticated() and #id == authentication.getPrincipal()")
    @PutMapping(value = "/v1/users/{id}/preference")
    public void updatePreference(final @PathVariable("id") long id, final Authentication authentication, @RequestBody @Valid final UserPreferenceListDTO userPreferenceDTOList) {
    //public void updatePreference(final @PathVariable("id") long id, final Authentication authentication, @RequestBody final List<UserPreferenceDTO> userPreferenceDTOList) {
        userPreferenceService.updatePreferences((Long) authentication.getPrincipal(), userPreferenceMapper.INSTANCE.toUserPreferenceVOList(userPreferenceDTOList.getUserPreferences()));
    }

    @PreAuthorize("isAuthenticated() and #id == authentication.getPrincipal()")
    @GetMapping(value = "/v1/users/{id}/preference")
    public String viewPreference(final @PathVariable("id") long id, final Authentication authentication) {

        String result = userPreferenceService.viewPreference((Long) authentication.getPrincipal());
        if (result.isEmpty()) {
            throw new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION.getMessage());
        }
        return result;
    }

    @PreAuthorize("isAuthenticated() and #id == authentication.getPrincipal()")
    @PatchMapping(value = "/v1/users/{id}/preference/prior")
    public void updatePreferencePriority(final @PathVariable("id") long id, final Authentication authentication, @RequestBody final List<UserPreferenceDTO> userPreferenceDTOList) {
        userPreferenceService.updatePreferencesPriority((Long) authentication.getPrincipal(), userPreferenceMapper.INSTANCE.toUserPreferenceVOList(userPreferenceDTOList));
    }
}
