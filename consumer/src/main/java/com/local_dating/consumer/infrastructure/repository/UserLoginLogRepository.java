package com.local_dating.consumer.infrastructure.repository;

import com.local_dating.consumer.domain.entity.UserLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginLogRepository extends JpaRepository<UserLoginLog, Long> {
}
