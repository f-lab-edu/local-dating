package com.local_dating.user_service.domain.vo;

public record UserProfileCoreVO(Long userId, String gender, Short birth, Short height, String education, Long salary,
                                String region) {
}
