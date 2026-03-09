package com.local_dating.user_service.application;

import com.local_dating.user_service.infrastructure.repository.PricingPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPricingPolicyService {

    private final PricingPolicyRepository pricingPolicyRepository;

    public Long viewPricingPolicy(final String targetCd) {
        return pricingPolicyRepository.getPricingPolicy(targetCd).getPrice();
    }
}
