package com.local_dating.matching_service.infrastructure;

import com.local_dating.matching_service.application.MatchingScheduleRequestedService;
import com.local_dating.matching_service.domain.event.CheckRoundScheduleEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckRoundScheduleEventHandler {

    private final MatchingScheduleRequestedService matchingScheduleRequestedService;

    @Async("roundScheduleExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(CheckRoundScheduleEvent event) {
        try {
            log.info("checkRoundSchedule start. matchingId={}, thread={}", event.matchingId(), Thread.currentThread().getName());
            matchingScheduleRequestedService.checkRoundSchedule(event.matchingId());
        } catch (Exception e) {
            log.error("라운드스케줄 공통 체크 실패. matchingId={}", event.matchingId(), e);
        }
    }
}
