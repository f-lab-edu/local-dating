package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.UserProfileCore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserProfileCoreRepository extends JpaRepository<UserProfileCore, Long>, UserProfileCoreRepositoryCustom {
    Optional<UserProfileCore> findByUserId(Long id);
}
