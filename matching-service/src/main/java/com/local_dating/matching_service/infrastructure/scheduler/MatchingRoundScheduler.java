package com.local_dating.matching_service.infrastructure.scheduler;

import com.local_dating.matching_service.application.MatchingServiceBatch;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class MatchingRoundScheduler {

    private final MatchingServiceBatch matchingServiceBatch;

    //@Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul") // 1분
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul") // 00시
    public void updateExpiredRounds() {
        matchingServiceBatch.updateExpiredRounds(LocalDate.now());
    }
}
