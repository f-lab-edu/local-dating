package com.local_dating.user_service.presentation.dto;

public record UserPreferenceDTO(String userId, String prefCd, String prefVal, int prior) {
}
