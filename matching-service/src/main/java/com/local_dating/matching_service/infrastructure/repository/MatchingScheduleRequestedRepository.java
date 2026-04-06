package com.local_dating.matching_service.infrastructure.repository;

import com.local_dating.matching_service.domain.entity.MatchingScheduleRequested;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchingScheduleRequestedRepository extends JpaRepository<MatchingScheduleRequested, Long> {

    List<MatchingScheduleRequested> findByMatchingIdAndMatchingScheduleRoundIdAndUserId(Long matchingId, Long matchingScheduleRoundId, Long userId);

    Optional<MatchingScheduleRequested> findByMatchingIdAndMatchingScheduleRoundIdAndUserIdAndMatchingDateAndMatchingTimeTypeAndStatusCd(Long matchingId, Long matchingScheduleRoundId
            , Long userId, LocalDate matchingDate, String matchingTimeType, String statusCd
    );
}
