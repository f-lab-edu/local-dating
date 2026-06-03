package com.local_dating.matching_service.infrastructure.scheduler;

import com.local_dating.matching_service.application.MatchingServiceBatch;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class MatchingRoundScheduler {

    private final MatchingServiceBatch matchingServiceBatch;

    //@Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul") // 1분
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul") // 00시
    @SchedulerLock(
            name = "matchingRoundScheduler.updateExpiredRounds",
            //lockAtMostFor = "50s",
            //lockAtLeastFor = "10s"
            lockAtMostFor = "30m",
            lockAtLeastFor = "5m"
    )
    public void updateExpiredRounds() {
        matchingServiceBatch.updateExpiredRounds(LocalDate.now(ZoneId.of("Asia/Seoul")));
    }
}
