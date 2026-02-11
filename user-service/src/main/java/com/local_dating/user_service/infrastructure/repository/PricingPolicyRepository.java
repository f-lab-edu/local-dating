package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.PricingPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricingPolicyRepository extends JpaRepository<PricingPolicy, Long>, PricingPolicyRepositoryCustom {
}
