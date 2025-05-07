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

    public Optional<MatchingSchedule> findMatchingScheduleByIdAndScheduleId(Long id, Long scheduleId);

    public Optional<MatchingSchedule> findMatchingScheduleByIdAndScheduleIdAndScheduleStatus(Long id, Long scheduleId, String status);

    public Optional<MatchingSchedule> findMatchingScheduleByIdAndUserId(Long id, Long userId);

    public List<MatchingSchedule> findMatchingSchedulesByMatchingIdAndUserId(Long id, Long userId);

    public List<MatchingSchedule> findMatchingSchedulesByMatchingIdAndScheduleStatus(Long matchingId, String scheduleStatus);
    public List<MatchingSchedule> findMatchingSchedulesByMatchingIdAndUserIdAndScheduleStatus(Long matchingId, Long userId, String scheduleStatus);

    public Optional<MatchingSchedule> findMatchingScheduleByMatchingIdAndUserIdAndMatchingDateAndMatchingTime(Long id, Long userId, LocalDate matchingDate, LocalTime matchingTime);
}
