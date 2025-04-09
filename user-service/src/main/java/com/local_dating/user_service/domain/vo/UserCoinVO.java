package com.local_dating.user_service.domain.vo;

import com.local_dating.user_service.domain.type.CoinActionType;

public record UserCoinVO(Long userId, Long balance, CoinActionType coinActionType) {
}
