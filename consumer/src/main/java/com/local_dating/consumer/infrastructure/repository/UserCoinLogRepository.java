package com.local_dating.consumer.infrastructure.repository;

import com.local_dating.consumer.domain.entity.UserCoinLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCoinLogRepository extends JpaRepository<UserCoinLog, Long> {
}
