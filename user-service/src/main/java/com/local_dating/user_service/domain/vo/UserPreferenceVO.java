package com.local_dating.user_service.domain.vo;

public record UserPreferenceVO(String userId, String prefCd, String prefVal, int prior) {
}
