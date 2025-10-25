package com.local_dating.user_service.application.async;

import com.local_dating.user_service.application.UserVerificationCommonService;
import com.local_dating.user_service.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserVerificationAsyncTask {

    private final UserVerificationCommonService userVerificationCommonService;

    @Async
    public CompletableFuture<User> verifyUserByPhoneAsync(final String phone) {
        log.info("verifyUserByPhoneAsync thread={}", Thread.currentThread().getName());
        return CompletableFuture.completedFuture(userVerificationCommonService.verifyUserByPhone(phone));
    }

    @Async
    public CompletableFuture<User> verifyUserByIdAsync(final Long id) {
        log.info("verifyUserByIdAsync thread={}", Thread.currentThread().getName());
        return CompletableFuture.completedFuture(userVerificationCommonService.verifyUserById(id));
    }

    @Async
    public CompletableFuture<Void> checkVerificationCodeAsync(final String code, final String phone) {
        log.info("checkVerificationCodeByPhoneAsync thread={}", Thread.currentThread().getName());
        userVerificationCommonService.checkVerificationCode(code, phone);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> checkVerificationCodeAsync(final String code, final Long id) {
        log.info("checkVerificationCodeByIdAsync thread={}", Thread.currentThread().getName());
        userVerificationCommonService.checkVerificationCode(code, id);
        return CompletableFuture.completedFuture(null);
    }
}
