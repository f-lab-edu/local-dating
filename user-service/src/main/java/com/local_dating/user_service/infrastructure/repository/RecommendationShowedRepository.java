package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.RecommendationShowed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationShowedRepository extends JpaRepository<RecommendationShowed, Long> {
}
