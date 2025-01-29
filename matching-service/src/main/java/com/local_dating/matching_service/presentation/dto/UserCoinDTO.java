package com.local_dating.matching_service.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserCoinDTO(@JsonProperty("userId") String userId, @JsonProperty("balance") Long balance) {
}
