package com.local_dating.user_service.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDTO(Long no,
                      @Size(max = 20, message = "id길이 초과") @NotBlank(message = "id빈값") @JsonProperty("loginId") String loginId,
                      @Size(max = 20, message = "pwd길이 초과") @NotBlank(message = "pwd빈값") String pwd, String name,
                      String nickname, String birth, String phone) {
}
