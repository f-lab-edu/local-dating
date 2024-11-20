package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.UserRecomCard;
import com.local_dating.user_service.domain.vo.UserRecomWithUserProfileVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRecomCardRepository extends JpaRepository<UserRecomCard, Long> {
    public UserRecomCard findByUserId(String userId);


    @Query("SELECT new com.local_dating.user_service.domain.vo.UserRecomWithUserProfileVO(u.cardId, u.useYn, p.infoCd, p.infoVal) FROM UserRecomCard u LEFT JOIN u.userProfiles p WHERE u.cardId = :userId")
    //오류//@Query("SELECT new com.local_dating.user_service.domain.vo.UserRecomWithUserProfileVO(u.cardId, u.useYn, p.infoCd, p.infoVal) FROM UserRecomCard u LEFT JOIN FETCH u.userProfiles p WHERE u.cardId = :userId")
    List<UserRecomWithUserProfileVO> findUserRecomCardWithUserProfile(@Param("userId") String targetId);
    //@Query("SELECT u FROM UserRecomCard u LEFT JOIN FETCH u.userProfiles WHERE u.cardId = :userId")
    //List<UserRecomCard> findUserRecomCardWithUserProfile(@Param("userId") String targetId);
}
