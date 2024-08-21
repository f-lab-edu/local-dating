package com.local_dating.user_service.domain.repository;

import com.local_dating.user_service.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserid(String userid);
}
