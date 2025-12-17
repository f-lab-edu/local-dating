package com.local_dating.user_service.domain.vo;

public record UserPreferenceCoreVO(Long userId, String gender, Short birthMin, Short birthMax, Short heightMin,
                                   Short heightMax, String education,
                                   Short salaryMin, Short salaryMax, Short rangeMax) {
}
