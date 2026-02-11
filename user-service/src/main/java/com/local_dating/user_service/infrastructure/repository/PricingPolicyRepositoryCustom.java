package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.PricingPolicy;

public interface PricingPolicyRepositoryCustom {
    PricingPolicy getPricingPolicy(String targetCd);
}
