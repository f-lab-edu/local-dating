package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.QUserPreference;
import com.local_dating.user_service.domain.entity.UserPreference;
import com.local_dating.user_service.domain.vo.UserPreferenceCountVO2;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserPreferenceRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<UserPreference> findMajorPrior(Long userId) {
        QUserPreference userPreference = QUserPreference.userPreference;

        return queryFactory
                .select(userPreference)
                .from(userPreference)
                .where(userPreference.prior.in(1, 2, 3).and(userPreference.userId.eq(userId)))
                .fetch();
    }

    public List<UserPreference> findMinorPrior(Long userId) {
        QUserPreference userPreference = QUserPreference.userPreference;

        return queryFactory
                .selectFrom(userPreference)
                .where(userPreference.prior.notIn(1, 2, 3).and(userPreference.userId.eq(userId)))
                .fetch();
    }

    public List<UserPreferenceCountVO2> findRecommendUser(Long userId, List<UserPreference> userPreferenceList) {
    //public List<UserPreference> findRecommendUser(String userId, List<UserPreference> userPreferenceList) {
        QUserPreference userPreference = QUserPreference.userPreference;

        BooleanExpression expression = null;

        for (UserPreference item : userPreferenceList) {
            BooleanExpression condition = userPreference.prefCd.eq(item.getPrefCd())
                    .and(userPreference.prefVal.eq(item.getPrefVal()));
                    //.and(userPreference.userId.ne(userId));

            // expression이 null이면 첫 조건으로 설정하고, 그렇지 않으면 OR로 결합
            expression = (expression == null) ? condition : expression.or(condition);
        }

        return queryFactory
                .select(Projections.constructor(UserPreferenceCountVO2.class,
                //.select(Projections.constructor(UserPreferenceCountVO.class,
                        userPreference.userId, userPreference.userId.count()))
                .from(userPreference)
                //.selectFrom(userPreference)
                .where(expression.and(userPreference.userId.ne(userId)))
                .groupBy(userPreference.userId)
                //.where(userPreference.prefCd.in(1, 2, 3).and(userPreference.userId.ne(userId)))
                .fetch();
    }

    public List<UserPreferenceCountVO2> findRecommendUserAlter(Long userId) {
        QUserPreference userPreference = QUserPreference.userPreference;

        return queryFactory
                .select(Projections.constructor(UserPreferenceCountVO2.class,
                        userPreference.userId, userPreference.userId.count()))
                .from(userPreference)
                .where(userPreference.userId.ne(userId))
                .groupBy(userPreference.userId)
                //.orderBy(userPreference.modDate.desc())
                .fetch();
    }

}
