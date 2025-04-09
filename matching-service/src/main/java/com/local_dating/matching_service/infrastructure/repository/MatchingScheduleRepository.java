package com.local_dating.matching_service.infrastructure.repository;

import com.local_dating.matching_service.domain.entity.MatchingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchingScheduleRepository extends JpaRepository<MatchingSchedule, Long> {

    public Optional<MatchingSchedule> findMatchingScheduleByIdAndUserId(Long id, Long userId);

    public List<MatchingSchedule> findMatchingSchedulesByIdAndUserId(Long id, Long userId);

    public Optional<MatchingSchedule> findMatchingScheduleByMatchingIdAndUserIdAndMatchingDateAndMatchingTime(Long id, Long userId, LocalDate matchingDate, LocalTime matchingTime);
}
