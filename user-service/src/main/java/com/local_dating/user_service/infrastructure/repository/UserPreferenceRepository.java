package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
    List<UserPreference> findByUserId(Long userId);
    Optional<UserPreference> findByUserIdAndPrefCd(Long userId, String prefCd);
    //List<UserPreference> findByUserIdAndPrefCd(String userId, String prefCd);

    //Optional<UserPreference> save(UserPreference userPreference);
}
