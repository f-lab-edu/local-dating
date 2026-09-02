package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.UserCoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserCoinRepository extends JpaRepository<UserCoin, Long> {
    Optional<UserCoin> findByUserId(Long userId);

    @Modifying
    @Query("UPDATE UserCoin c SET c.balance = c.balance + :balance WHERE c.userId = :userId")
    void addCoin(@Param("userId") Long userId, @Param("balance") Long balance);

    @Modifying
    @Query("UPDATE UserCoin c SET c.balance = c.balance - :balance WHERE c.userId = :userId AND c.balance >= :balance")
    void consumeCoin(@Param("userId") Long userId, @Param("balance") Long balance);
}
