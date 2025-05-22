package com.local_dating.matching_service.infrastructure.repository;

import com.local_dating.matching_service.domain.entity.CoinPolicy;
import com.local_dating.matching_service.domain.entity.QCoinPolicy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class CoinPolicyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CoinPolicy getValidPrice(LocalDate now) {
        QCoinPolicy coinPolicy = QCoinPolicy.coinPolicy;

        return queryFactory
                .selectFrom(coinPolicy)
                .where(
                        (coinPolicy.startDate.loe(now))
                                .and(coinPolicy.startDate.goe(now))
                                .and(coinPolicy.useYn.eq("Y"))
                )
                .fetchFirst();
    }

}
