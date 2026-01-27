package com.local_dating.matching_service.application;

import com.local_dating.matching_service.util.UserServiceClient;
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
    //private final RedisTemplate<String, String> redisTemplate;
    private final UserServiceClient userServiceClient;

    //public String searchNext(final Long userNo) {
    public String searchNext(final Long userNo, final String authentication) {
        String key = "userSearchNext:" + userNo; //deckKey(userNo);
        String nextId = stringRedisTemplate.opsForList().rightPop(key);

        if (nextId == null) {
            List<String> list = userServiceClient.searchNext(userNo, authentication).stream()
                    .map(el -> String.valueOf(el.userId()))
                    .collect(Collectors.toUnmodifiableList());
            stringRedisTemplate.opsForList().rightPushAll(key, list);

            stringRedisTemplate.expire(key, 3, TimeUnit.DAYS);
            nextId = stringRedisTemplate.opsForList().rightPop(key);
        }
        stringRedisTemplate.opsForValue().set("userSearchCurrent:" + userNo, nextId);



        return nextId;

        /*
        if (nextId != null) {
            //레디스 있음

            return nextId;
        } else {
            //레디스 없음, 가져오기
            //List<String> list = userServiceClient.searchNext(userNo).stream()
            List<String> list = userServiceClient.searchNext(userNo, authentication).stream()
                    .map(el -> String.valueOf(el.userId()))
                    .collect(Collectors.toUnmodifiableList());
            //List<String> values = candidates.stream().map(String::valueOf).toList();
            stringRedisTemplate.opsForList().rightPushAll(key, list);

            stringRedisTemplate.expire(key, 3, TimeUnit.DAYS);
            nextId = stringRedisTemplate.opsForList().rightPop(key);
            return nextId;
        }
        */

    }

    public String searchCurrent(final Long userNo, final String authentication) {
        String key = "userSearchCurrent:" + userNo;
        String currentId = stringRedisTemplate.opsForValue().get(key);

        if (currentId == null) {
            currentId = searchNext(userNo, authentication);
        }
        return currentId;
    }
}
