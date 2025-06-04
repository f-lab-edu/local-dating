package com.local_dating.user_service.application;

import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class MessageService {

    public CompletableFuture<Void> sendVerificationEmail(String email, String code) {
        return CompletableFuture.runAsync(() -> {
            try {
                log.info("sendVerificationEmail 시간: " + LocalDateTime.now());
                log.info("스레드: " + Thread.currentThread().getName());

                /*if (true) {
                    throw new RuntimeException("강제 예외 발생");
                }*/
                //Thread.sleep(5000); // 테스트

                log.info("대상메일: " + email);
                log.info("발송코드: " + code);

            } catch (Exception e) {
                throw new BusinessException(MessageCode.SEND_MESSAGE_FAIL);
                //RuntimeException("이메일 발송 실패: " + e.getMessage(), e);
            }
        });
    }
    /*
    @Async
    public void sendVerificationEmail(String email, String code) {

        log.info("sendVerificationEmail 시간: " + LocalDateTime.now());
        log.info("스레드: " + Thread.currentThread().getName());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("대상메일: " + email);
        log.info("발송코드: " + code);

    }
    */
}
