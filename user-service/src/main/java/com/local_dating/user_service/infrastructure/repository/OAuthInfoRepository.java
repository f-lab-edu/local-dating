package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.OAuthInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OAuthInfoRepository extends JpaRepository<OAuthInfo, Long> {
    List<OAuthInfo> findByUserNo(Long userId);

    Optional<OAuthInfo> findByProviderId(String providerId);

    Optional<OAuthInfo> findByEmail(String email);

    Optional<OAuthInfo> findByProviderAndProviderId(String provider, String providerId);

    Optional<OAuthInfo> findByUserNoAndProvider(Long userNo, String provider);
}
