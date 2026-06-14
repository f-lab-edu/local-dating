package com.local_dating.user_service.infrastructure.repository;

import com.local_dating.user_service.domain.entity.UserMeetingLocationPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMeetingLocationPreferenceRepository extends JpaRepository<UserMeetingLocationPreference, Long> {

    List<UserMeetingLocationPreference> findByUserIdAndActiveYnOrderByPriorityAsc(Long userId, String activeYn);

    Optional<UserMeetingLocationPreference> findByIdAndUserId(Long id, Long userId);

    Long countByUserIdAndActiveYn(Long userId, String activeYn);
}
