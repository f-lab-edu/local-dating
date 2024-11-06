package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.QUserPreference;
import com.local_dating.user_service.domain.entity.QUserRecomCard;
import com.local_dating.user_service.domain.entity.UserPreference;
import com.local_dating.user_service.domain.entity.UserRecomCard;
import com.local_dating.user_service.domain.vo.UserPreferenceCountVO2;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRecomCardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<UserRecomCard> findValidCard(String userId) {

        QUserRecomCard userRecomCard = QUserRecomCard.userRecomCard;

        //LocalDateTime now = LocalDateTime.now();
        //LocalDateTime endDate = now.minusDays(14);
        LocalDateTime endDate = LocalDateTime.now().minusDays(14);

        return queryFactory
                .select(userRecomCard)
                .from(userRecomCard)
                .where(userRecomCard.userId.eq(userId)
                                .and(userRecomCard.useyn.eq("Y"))
                                .and(userRecomCard.inDate.gt(endDate))
                                //.and(Expressions.dateTemplate(LocalDateTime.class, "DATE_ADD({0}, INTERVAL 14 DAY)", userRecomCard.inDate).gt(LocalDateTime.now()))
                                //.and(userRecomCard.inDate.between(userRecomCard.inDate, userRecomCard.inDate.pl))
                )
                .fetch();
    }

    public List<UserPreference> findMinorPrior(String userId) {
        QUserPreference userPreference = QUserPreference.userPreference;

        return queryFactory
                .selectFrom(userPreference)
                .where(userPreference.prior.notIn(1, 2, 3).and(userPreference.userId.eq(userId)))
                .fetch();
    }

    public List<UserPreferenceCountVO2> findRecommendUser(String userId, List<UserPreference> userPreferenceList) {
    //public List<UserPreference> findRecommendUser(String userId, List<UserPreference> userPreferenceList) {
        QUserPreference userPreference = QUserPreference.userPreference;

        BooleanExpression expression = null;

        for (UserPreference item : userPreferenceList) {
            BooleanExpression condition = userPreference.prefCd.eq(item.getPrefCd())
                    .and(userPreference.prefVal.eq(item.getPrefVal()));

            // expression이 null이면 첫 조건으로 설정하고, 그렇지 않으면 OR로 결합
            expression = (expression == null) ? condition : expression.or(condition);
        }

        return queryFactory
                .select(Projections.constructor(UserPreferenceCountVO2.class,
                //.select(Projections.constructor(UserPreferenceCountVO.class,
                        userPreference.userId, userPreference.userId.count()))
                .from(userPreference)
                //.selectFrom(userPreference)
                .where(expression)
                .groupBy(userPreference.userId)
                //.where(userPreference.prefCd.in(1, 2, 3).and(userPreference.userId.ne(userId)))
                .fetch();
    }

}
