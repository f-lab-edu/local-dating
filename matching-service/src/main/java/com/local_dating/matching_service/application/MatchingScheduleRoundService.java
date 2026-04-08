package com.local_dating.matching_service.application;

import com.local_dating.matching_service.domain.entity.MatchingScheduleRound;
import com.local_dating.matching_service.infrastructure.repository.MatchingScheduleRoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MatchingScheduleRoundService {

    private final MatchingScheduleRoundRepository matchingScheduleRoundRepository;

    public void saveMatchingScheduleRound(Long matchingId, Integer round, String statusCd, LocalDate roundStartDate, LocalDate roundEndDate, Long userId) {
        matchingScheduleRoundRepository.save(new MatchingScheduleRound(
                matchingId,
                round,
                statusCd,
                roundStartDate,
                roundEndDate,
                userId
        ));
    }
}
