package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.PricingPolicy;
import com.local_dating.user_service.domain.entity.QPricingPolicy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class PricingPolicyRepositoryCustomImpl implements PricingPolicyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public PricingPolicy getPricingPolicy(String targetCd) {

        QPricingPolicy pricingPolicy = QPricingPolicy.pricingPolicy;
        LocalDateTime now = LocalDateTime.now();

        return queryFactory.selectFrom(pricingPolicy)
                .where(pricingPolicy.targetCd.eq(targetCd)
                        , pricingPolicy.startDate.loe(now)
                        , pricingPolicy.endDate.gt(now))
                .fetchFirst();

    }
}
