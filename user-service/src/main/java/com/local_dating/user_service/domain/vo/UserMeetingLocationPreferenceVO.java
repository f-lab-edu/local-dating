package com.local_dating.user_service.domain.vo;

import java.math.BigDecimal;

public record UserMeetingLocationPreferenceVO(Long userId, String areaName, String address, String addressDetail1, String addressDetail2,
        BigDecimal latitude, BigDecimal longitude, int priority, String activeYn) {
}
