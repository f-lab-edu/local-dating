package com.local_dating.user_service.presentation.dto;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

@Validated
public record UserValidationDTO(Long no, @NotEmpty(message = "phone 빈값") String phone, String code) {
}
