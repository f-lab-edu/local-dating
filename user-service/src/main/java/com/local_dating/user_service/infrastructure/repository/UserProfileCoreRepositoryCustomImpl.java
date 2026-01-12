package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.QUser;
import com.local_dating.user_service.domain.entity.QUserProfileCore;
import com.local_dating.user_service.domain.entity.UserProfileCore;
import com.local_dating.user_service.domain.vo.UserPreferenceCoreVO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserProfileCoreRepositoryCustomImpl implements UserProfileCoreRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<UserProfileCore> searchNextUsers(Long userNo, UserPreferenceCoreVO pref, int limit) {

        QUserProfileCore userProfileCore = QUserProfileCore.userProfileCore;
        QUser user = QUser.user;

        BooleanBuilder where = new BooleanBuilder();

        // 자기 자신 제외
        where.and(user.no.ne(userNo));

        // 동적 선호 조건 (null/빈값 무시)
        if (pref.gender() != null && !pref.gender().isEmpty()) {
            where.and(userProfileCore.gender.eq(pref.gender()));
        }
        if (pref.birthMin() != null) {
            where.and(userProfileCore.birth.goe(pref.birthMin())); // >=
        }
        if (pref.birthMax() != null) {
            where.and(userProfileCore.birth.loe(pref.birthMax())); // <=
        }
        if (pref.heightMin() != null) {
            where.and(userProfileCore.height.goe(pref.heightMin()));
        }
        if (pref.heightMax() != null) {
            where.and(userProfileCore.height.loe(pref.heightMax()));
        }
        if (pref.education() != null && !pref.education().isEmpty()) {
            where.and(userProfileCore.education.eq(pref.education()));
        }
        if (pref.salaryMin() != null) {
            where.and(userProfileCore.salary.goe(pref.salaryMin().longValue()));
        }
        if (pref.salaryMax() != null) {
            where.and(userProfileCore.salary.loe(pref.salaryMax().longValue()));
        }

        return queryFactory
                .selectFrom(userProfileCore)
                .join(user).on(user.no.eq(userProfileCore.userId))
                .where(where)
                .orderBy(user.lastLoginDate.desc())
                .limit(limit)
                .fetch();
    }
}
