package com.local_dating.user_service.presentation.dto;

public record UserPreferenceCoreDTO(Long userId, String gender, Short birthMin, Short birthMax, Short heightMin,
                                    Short heightMax, String education,
                                    Short salaryMin, Short salaryMax, Short rangeMax) {
}
