package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);
    //Optional<User> findByUserId(String userId);
}
