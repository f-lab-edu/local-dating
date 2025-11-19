package com.local_dating.user_service.application.async;

import com.local_dating.user_service.application.common.UserVerificationCommonService;
import com.local_dating.user_service.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserVerificationAsyncTask {

    private final UserVerificationCommonService userVerificationCommonService;
    private final Executor threadPoolTaskExecutor;

    public CompletableFuture<User> verifyUserByPhoneAsync(final String phone) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("verifyUserByPhoneAsync thread={}", Thread.currentThread().getName());
            return userVerificationCommonService.verifyUserByPhone(phone);
        }, threadPoolTaskExecutor);
    }

    public CompletableFuture<User> verifyUserByIdAsync(final Long id) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("verifyUserByIdAsync thread={}", Thread.currentThread().getName());
            return userVerificationCommonService.verifyUserById(id);
        }, threadPoolTaskExecutor);
    }

    public CompletableFuture<Void> checkVerificationCodeAsync(final String code, final String phone) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("checkVerificationCodeByPhoneAsync thread={}", Thread.currentThread().getName());
            userVerificationCommonService.checkVerificationCode(code, phone);
            return null;
        });
    }

    public CompletableFuture<Void> checkVerificationCodeAsync(final String code, final Long id) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("checkVerificationCodeByIdAsync thread={}", Thread.currentThread().getName());
            userVerificationCommonService.checkVerificationCode(code, id);
            return null;
        });
    }

}
