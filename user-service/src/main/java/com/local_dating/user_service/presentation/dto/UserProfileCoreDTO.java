package com.local_dating.user_service.presentation.dto;

public record UserProfileCoreDTO(Long userId, String gender, Short birth, Short height, String education,
                                 Short salary, String region) {
}
