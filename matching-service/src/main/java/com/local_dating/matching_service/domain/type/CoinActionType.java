package com.local_dating.matching_service.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CoinActionType {
    CHARGE("CHARGE", "충전")
    , CONSUME("CONSUME", "소모")
    , RESTORE("RESTORE", "복구")
    , TICKET_PRICE("10000", "티켓가격");

    private final String code;
    private final String message;

}
