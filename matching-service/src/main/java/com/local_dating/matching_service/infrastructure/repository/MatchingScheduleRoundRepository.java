package com.local_dating.matching_service.infrastructure.repository;

import com.local_dating.matching_service.domain.entity.MatchingScheduleRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchingScheduleRoundRepository extends JpaRepository<MatchingScheduleRound, Long> {

    Optional<MatchingScheduleRound> findByIdAndMatchingIdAndStatusCd(Long id, Long matchingId, String statusCd);

    List<MatchingScheduleRound> findByIdInAndMatchingIdInAndStatusCd(List<Long> idList, List<Long> matchingIdList, String statusCd);

    Optional<MatchingScheduleRound> findByMatchingIdAndRound(Long matchingId, Long round);
}
