package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {
    //UserProfile save(UserProfile userProfile);
    //int save(UserProfileVO userProfileVO);
}
