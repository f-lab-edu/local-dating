package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.UserLoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginLogRepository extends JpaRepository<UserLoginLog, Long> {
}
