package com.local_dating.user_service.presentation.controller;

import com.local_dating.user_service.application.UserPreferenceService;
import com.local_dating.user_service.domain.entity.UserPreference;
import com.local_dating.user_service.domain.mapper.UserPreferenceMapper;
import com.local_dating.user_service.presentation.dto.UserPreferenceDTO;
import com.local_dating.user_service.util.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.local_dating.user_service.util.MessageCode.DATA_NOT_FOUND_EXCEPTION;

@RestController
@RequiredArgsConstructor
public class PreferenceController {

    private final UserPreferenceMapper userPreferenceMapper;
    private final UserPreferenceService userPreferenceService;

    @PostMapping(value = "/v1/users/{id}/preference")
    public List savePreference(final Authentication authentication, @RequestBody final List<UserPreferenceDTO> userPreferenceDTOList) {
        List<UserPreference> userPreferenceList = userPreferenceService.savePreferences((String) authentication.getPrincipal(), UserPreferenceMapper.INSTANCE.toUserPreferenceVOList(userPreferenceDTOList));
        return userPreferenceList;
    }

    @PutMapping(value = "/v1/users/{id}/preference")
    public void updatePreference(final Authentication authentication, @RequestBody final List<UserPreferenceDTO> userPreferenceDTOList) {
        userPreferenceService.updatePreferences(authentication.getPrincipal().toString(), userPreferenceMapper.INSTANCE.toUserPreferenceVOList(userPreferenceDTOList));
    }

    @GetMapping(value = "/v1/users/{id}/preference")
    public String viewPreference(final Authentication authentication) {

        String result = userPreferenceService.viewPreference((String) authentication.getPrincipal());
        if (result.isEmpty()) {
            throw new DataNotFoundException(DATA_NOT_FOUND_EXCEPTION.getMessage());
        }
        return result;
    }

    @PatchMapping(value = "/v1/users/{id}/preference/prior")
    public void updatePreferencePriority(final Authentication authentication, @RequestBody final List<UserPreferenceDTO> userPreferenceDTOList) {
        userPreferenceService.updatePreferencesPriority(authentication.getPrincipal().toString(), userPreferenceMapper.INSTANCE.toUserPreferenceVOList(userPreferenceDTOList));
    }
}
