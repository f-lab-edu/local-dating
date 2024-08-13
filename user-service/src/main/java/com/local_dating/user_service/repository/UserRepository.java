package com.local_dating.user_service.repository;

import com.local_dating.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserid(String userid);
}
