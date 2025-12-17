package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.UserPreferenceCore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPreferenceCoreRepository extends JpaRepository<UserPreferenceCore, Long> {
    Optional<UserPreferenceCore> findByUserId(Long userId);
}
