package com.local_dating.user_service.domain.vo;

import java.io.Serializable;

public record UserPreferenceCountVO(String userId, int count) implements Serializable {
}
