package com.local_dating.matching_service.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CoinActionType {
    CHARGE("CHARGE", "충전")
    , CONSUME("CONSUME", "소모")
    , RECHARGE("RECHARGE", "복구");

    private final String code;
    private final String message;

}
