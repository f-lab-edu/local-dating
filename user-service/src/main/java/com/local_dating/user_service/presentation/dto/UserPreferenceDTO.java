package com.local_dating.user_service.presentation.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record UserPreferenceDTO(String userId,
                                @Size(max = 100, message = "코드 길이 초과") @NotBlank(message = "코드 빈값") String prefCd,
                                @Size(max = 200, message = "코드값 길이 초과") @NotBlank(message = "코드값 빈값") String prefVal,
                                @PositiveOrZero @Max(Integer.MAX_VALUE) @NotBlank(message = "우선순위 빈값") int prior) {
}
