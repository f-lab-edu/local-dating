package com.local_dating.user_service.presentation.dto;

import java.math.BigDecimal;

public record UserMeetingLocationPreferenceDTO(Long userId, String areaName, String address, String addressDetail1
        , String addressDetail2, BigDecimal latitude, BigDecimal longitude, int priority) {
}
