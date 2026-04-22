package com.local_dating.matching_service.application;

import com.local_dating.matching_service.domain.vo.UserProfileCoreVO;
import com.local_dating.matching_service.presentation.dto.UserCoinDTO;
import com.local_dating.matching_service.util.UserServiceClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.CircuitBreakingException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.local_dating.matching_service.util.MessageCode.EXTERNAL_EXCEPTION;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceClientWithCircuitBreaker {

    private final UserServiceClient userServiceClient;

    @CircuitBreaker(name = "user-service", fallbackMethod = "viewCoinPolicyFail")
    public Long viewCoinPolicy(final long id, final String type, final String authentication) {
        return userServiceClient.viewPricingPolicy(id, type, authentication);
    }

    @CircuitBreaker(name = "user-service", fallbackMethod = "viewCoinFail")
    public Long viewCoin(final long id, final String authentication) {
        return userServiceClient.viewCoin(id, authentication);
    }

    @CircuitBreaker(name = "user-service", fallbackMethod = "saveCoinFail")
    public void saveCoin(final long id, final String authentication, final UserCoinDTO userCoinDTO) {
        userServiceClient.saveCoin(id, authentication, userCoinDTO);
    }

    @CircuitBreaker(name = "user-service", fallbackMethod = "updateCoinFail")
    public void updateCoin(final long id, final String authentication, final UserCoinDTO userCoinDTO) {
        userServiceClient.updateCoin(id, authentication, userCoinDTO);
    }

    public List<UserProfileCoreVO> searchNext(final Long id, final String authentication) {
        return userServiceClient.searchNext(id, authentication);
    }

    @CircuitBreaker(name = "user-service", fallbackMethod = "validateUserIdFail")
    public void validateUserId(final long id, final String authentication) {
        userServiceClient.validateUserId(id, authentication);
    }

    private Long viewCoinPolicyFail(final long id, final String type, final String authentication, Exception e) {
        log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
        throw new CircuitBreakingException(EXTERNAL_EXCEPTION.getMessage(), e);
    }

    private Long viewCoinFail(final long id, final String authentication, Exception e) {
        log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
        throw new CircuitBreakingException(EXTERNAL_EXCEPTION.getMessage(), e);
    }

    private void saveCoinFail(final long id, final String authentication, final UserCoinDTO userCoinDTO, Exception e) {
        log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
        throw new CircuitBreakingException(EXTERNAL_EXCEPTION.getMessage(), e);
    }

    private void updateCoinFail(final long id, final String authentication, final UserCoinDTO userCoinDTO, Exception e) {
        log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
        throw new CircuitBreakingException(EXTERNAL_EXCEPTION.getMessage(), e);
    }

    private void validateUserIdFail(final long id, final String authentication, Exception e) {
        log.error(Thread.currentThread().getStackTrace()[1].getMethodName(), e);
        throw new CircuitBreakingException(EXTERNAL_EXCEPTION.getMessage(), e);
    }
}
