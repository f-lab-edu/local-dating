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

    public Optional<MatchingSchedule> findMatchingScheduleByIdAndMatchingId(Long scheduleId, Long id);

    public Optional<MatchingSchedule> findMatchingScheduleByIdAndMatchingIdAndScheduleStatus(Long scheduleId, Long id, String status);

    public Optional<MatchingSchedule> findMatchingScheduleByIdAndUserId(Long id, Long userId);

    public Optional<MatchingSchedule> findMatchingScheduleByIdAndUserIdAndScheduleStatus(Long id, Long userId, String status);

    public List<MatchingSchedule> findMatchingSchedulesByMatchingIdAndUserId(Long id, Long userId);

    public List<MatchingSchedule> findMatchingSchedulesByMatchingIdAndScheduleStatus(Long matchingId, String scheduleStatus);
    public List<MatchingSchedule> findMatchingSchedulesByMatchingIdAndUserIdAndScheduleStatus(Long matchingId, Long userId, String scheduleStatus);

    public Optional<MatchingSchedule> findMatchingScheduleByMatchingIdAndUserIdAndMatchingDateAndMatchingTime(Long id, Long userId, LocalDate matchingDate, LocalTime matchingTime);
}
