package com.local_dating.matching_service.application;

import com.local_dating.matching_service.domain.entity.MatchingScheduleRound;
import com.local_dating.matching_service.domain.type.MatchingScheduleRoundType;
import com.local_dating.matching_service.domain.type.MatchingType;
import com.local_dating.matching_service.infrastructure.repository.MatchingRepository;
import com.local_dating.matching_service.infrastructure.repository.MatchingScheduleRoundRepository;
import com.local_dating.matching_service.util.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.local_dating.matching_service.util.MessageCode.VALIDATION_EXCEPTION_MATCHING;
import static com.local_dating.matching_service.util.MessageCode.VALIDATION_EXCEPTION_MATCHING_SCHEDULE_ROUND;

@Service
@RequiredArgsConstructor
public class MatchingServiceBatch {

    private final MatchingRepository matchingRepository;
    private final MatchingScheduleRoundRepository matchingScheduleRoundRepository;

    @Transactional
    public void updateExpiredRounds(LocalDate date) {

        matchingScheduleRoundRepository.findByRoundEndDateLessThanAndStatusCd(date, MatchingScheduleRoundType.OPEN.getCode())
                .forEach(round -> {
                    round.setStatusCd(MatchingScheduleRoundType.EXPIRED.getCode());

                    if (round.getRound() < 3 && round.getRound() > 0) { // 다음 라운드
                        matchingScheduleRoundRepository.save(
                                new MatchingScheduleRound(round.getMatchingId(), round.getRound() + 1, MatchingScheduleRoundType.OPEN.getCode()
                                        , round.getRoundEndDate().plusDays(1), round.getRoundEndDate().plusDays(3), round.getInUser())
                        );
                    } else if (round.getRound() == 3) { // 매칭 종료
                        matchingRepository.findById(round.getMatchingId()).map(matching -> {
                            matching.setStatusCd(MatchingType.SCHEDULE_EXPIRED.getCode());
                            return matching;
                        }).orElseThrow(() -> new BusinessException(VALIDATION_EXCEPTION_MATCHING, "matching: " + round.getMatchingId()));
                    } else {
                        throw new BusinessException(VALIDATION_EXCEPTION_MATCHING_SCHEDULE_ROUND, "matching: " + round.getMatchingId() + ", round: " + round.getRound());
                    }
                });
    }
}
