package com.local_dating.user_service.domain.vo;

import java.io.Serializable;

public record UserPreferenceVO(String userId, String prefCd, String prefVal, int prior) implements Serializable {
}
