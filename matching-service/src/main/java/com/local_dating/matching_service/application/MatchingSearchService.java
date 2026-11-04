package com.local_dating.matching_service.application;

import com.local_dating.matching_service.domain.RedisKeys;
import com.local_dating.matching_service.domain.type.CoinActionType;
import com.local_dating.matching_service.presentation.dto.UserCoinDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchingSearchService {

    private final StringRedisTemplate stringRedisTemplate;
    private final UserServiceClientWithCircuitBreaker userServiceClientWithCircuitBreaker;
    private final RedisKeys keys;

    //public String searchNext(final Long userNo) {
    public String searchNext(final Long userNo, final String authentication) {
        String key = keys.userSearchCurrent() + userNo; //deckKey(userNo);

        String nextId = stringRedisTemplate.opsForList().index(key, -1); // 리스트 여부 검사
        //String nextId = stringRedisTemplate.opsForList().rightPop(key);

        //userServiceClientWithCircuitBreaker.updateCoin(userNo, authentication, new UserCoinDTO(String.valueOf(userNo), -10000L, CoinActionType.CONSUME));

        if (nextId == null) {
            List<String> list = userServiceClientWithCircuitBreaker.searchNext(userNo, authentication).stream()
                    .map(el -> String.valueOf(el.userId()))
                    .collect(Collectors.toUnmodifiableList());

            if (list.isEmpty()) {
                return null; // 빈 추천리스트
            }
            stringRedisTemplate.opsForList().rightPushAll(key, list);

            stringRedisTemplate.expire(key, 3, TimeUnit.DAYS);
        }
        //    nextId = stringRedisTemplate.opsForList().rightPop(key);
        //} else {

        //잔액검사
        userServiceClientWithCircuitBreaker.consumeCoin(userNo, authentication, new UserCoinDTO(String.valueOf(userNo), -10000L, CoinActionType.CONSUME));
        //userServiceClientWithCircuitBreaker.updateCoin(userNo, authentication, new UserCoinDTO(String.valueOf(userNo), -10000L, CoinActionType.CONSUME));
        nextId = stringRedisTemplate.opsForList().rightPop(key);
        //}

        if (nextId == null) {
            return null;
        }

        String nowCardkey = keys.userNowSelected() + userNo;
        stringRedisTemplate.opsForValue().set(nowCardkey, nextId);

        return nextId;
    }

    public String searchCurrent(final Long userNo, final String authentication) {
        String nowCardkey = keys.userNowSelected() + userNo;
        String currentId = stringRedisTemplate.opsForValue().get(nowCardkey);
        //String currentId = stringRedisTemplate.opsForList().rightPop(key);

        if (currentId == null) {
            currentId = searchNext(userNo, authentication);
        }
        return currentId;
    }
}
