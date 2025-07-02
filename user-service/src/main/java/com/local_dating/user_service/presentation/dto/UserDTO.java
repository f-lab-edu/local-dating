package com.local_dating.user_service.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserDTO(Long no, @NotEmpty(message = "id빈값") @JsonProperty("loginId") String loginId, @NotNull(message = "pwd빈값") String pwd, String name, String nickname, String birth, String phone) {
//public record UserDTO(@NotEmpty(message = "id빈값") @JsonProperty("loginId") String loginId, @NotNull(message = "pwd빈값") String pwd, String name, String nickname, String birth, String phone) {
//public record UserDTO(@NotEmpty(message = "id빈값") @JsonProperty("userId") String userId, @NotNull(message = "pwd빈값") String pwd, String name, String nickname, String birth, String phone) {
}
