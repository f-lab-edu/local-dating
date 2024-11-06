package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.UserRecomCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRecomCardRepository extends JpaRepository<UserRecomCard, Long> {
    public UserRecomCard findByUserId(String userId);
}
