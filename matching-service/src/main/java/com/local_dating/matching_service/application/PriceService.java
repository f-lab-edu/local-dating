package com.local_dating.matching_service.application;

import com.local_dating.matching_service.domain.type.ItemType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final UserServiceClientWithCircuitBreaker userServiceClientWithCircuitBreaker;

    public Long viewPrice(final Long userId, final String authentication, final ItemType itemType) {
        return Long.valueOf(userServiceClientWithCircuitBreaker.viewCoinPolicy(userId, itemType.getCode(), authentication));
    }
}
