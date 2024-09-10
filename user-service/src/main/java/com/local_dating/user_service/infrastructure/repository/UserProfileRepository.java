package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {

    Optional<List<UserProfile>> findByUserId(String userId);
    //Optional<UserProfile> findByUserId(String userId);
    Optional<UserProfile> findByUserIdAndInfoCd(String userId, String infoCd);
    //Optional<UserProfile> findByUserIdAndInfoCdIn(String userId, List<UserProfile> infoCd);

    //UserProfile save(UserProfile userProfile);
    //int save(UserProfileVO userProfileVO);
}
