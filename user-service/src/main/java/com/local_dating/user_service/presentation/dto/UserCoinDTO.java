package com.local_dating.user_service.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.local_dating.user_service.domain.type.CoinActionType;

public record UserCoinDTO(@JsonProperty("userId") String userId, @JsonProperty("balance") Long balance, CoinActionType coinActionType) {
}
