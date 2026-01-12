package com.local_dating.matching_service.domain.vo;

public record UserProfileCoreVO(Long userId, String gender, Short birth, Short height, String education, Long salary,
                                String region) {
}
