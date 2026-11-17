package com.local_dating.matching_service.application;

import com.local_dating.matching_service.domain.entity.MatchSaga;
import com.local_dating.matching_service.domain.type.ItemType;
import com.local_dating.matching_service.domain.vo.CoinSpendRequestVO;
import com.local_dating.matching_service.domain.vo.MatchingVO;
import com.local_dating.matching_service.domain.vo.OperationResultVO;
import com.local_dating.matching_service.util.MessageCode;
import com.local_dating.matching_service.util.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchingOrchestrator {

    private final MatchingService matchingService;
    private final UserServiceClientWithCircuitBreaker userServiceClientWithCircuitBreaker;
    private final PriceService priceService;
    private final MatchSagaService matchSagaService;


    public MatchingVO requestMatching(final Long userId, final String authentication, final MatchingVO matchingVO) {

        if (!(userId != null && userId.equals(matchingVO.requId()))) {
            throw new BusinessException(MessageCode.MATCHING_ACCESS_DENIED);
        }

        userServiceClientWithCircuitBreaker.validateUserId(userId, authentication);
        userServiceClientWithCircuitBreaker.validateUserId(matchingVO.recvId(), authentication);

        Long price = priceService.viewPrice(userId, authentication, ItemType.SEARCHING);
        Long balance = userServiceClientWithCircuitBreaker.viewCoin(userId, authentication);

        if (balance == null || balance < price) {
            throw new BusinessException(MessageCode.INSUFFICIENT_COIN);
        }

        MatchSaga saga = matchSagaService.start(userId, matchingVO.recvId(), price); // 작업 기록

        OperationResultVO result = userServiceClientWithCircuitBreaker
                .spendCoin(authentication,
                        new CoinSpendRequestVO(UUID.fromString(saga.getSagaId()),
                                UUID.fromString(saga.getOperationId()),
                                   userId, saga.getAmount())
                );

        switch (result.status()) {
            case SUCCESS:
                return matchingService.createRequestedMatching(userId, matchingVO);

            case FAILURE:
                throw new BusinessException(MessageCode.INSUFFICIENT_COIN); // 실패 시 코인차감 문제

            case PENDING:
                throw new BusinessException(MessageCode.PENDING_COIN);

            default:
                throw new IllegalStateException("기타 오류");
        }
        /*
        userServiceClientWithCircuitBreaker.saveCoin(userId, authentication, new UserCoinDTO(String.valueOf(userId), -price, CoinActionType.CONSUME));
        return matchingService.createRequestedMatching(userId, matchingVO);
        */
    }
}
