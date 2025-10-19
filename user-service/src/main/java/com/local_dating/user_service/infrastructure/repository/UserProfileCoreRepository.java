package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.UserProfileCore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileCoreRepository extends JpaRepository<UserProfileCore, Long> {
}
