package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.UserCoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCoinRepository extends JpaRepository<UserCoin, Long> {
    Optional<UserCoin> findByUserId(Long userId);
}
