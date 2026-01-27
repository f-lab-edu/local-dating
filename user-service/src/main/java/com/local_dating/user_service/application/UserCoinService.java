package com.local_dating.user_service.application;

import com.local_dating.user_service.domain.entity.UserCoin;
import com.local_dating.user_service.domain.vo.UserCoinLogVO;
import com.local_dating.user_service.domain.vo.UserCoinVO;
import com.local_dating.user_service.infrastructure.repository.UserCoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserCoinService {

    private final UserCoinRepository userCoinRepository;
    private final KafkaProducer kafkaProducer;
    private final StringRedisTemplate stringRedisTemplate;

    //@Cacheable(value = "coin", key = "#userId")
    //@Cacheable(value = "coin", key = "#userId", cacheManager = "jsonCacheManager")
    public Long viewCoin(final Long userId) {

        String key = "coin:" + userId;
        String coin = stringRedisTemplate.opsForValue().get(key);

        if (coin != null) {
            return Long.parseLong(coin);
        }

        return userCoinRepository.findByUserId(userId).map(el->{
            stringRedisTemplate.opsForValue().set(key, String.valueOf(el.getBalance()));
            return el.getBalance();
        }).orElseGet(()->{
            return 0L;
        });
    }

    @Transactional
    //@CacheEvict(value = "coin", key = "#userId")
    public void saveCoin(final Long userId, final UserCoinVO userCoinVO) {

        stringRedisTemplate.delete("coin:" + userId);
        userCoinRepository.findByUserId(userId).map(el -> {
            el.setBalance(el.getBalance() + userCoinVO.balance());
            kafkaProducer.sendMessage("coin-topic", new UserCoinLogVO(userId, userCoinVO.balance(), "charge", LocalDateTime.now(), userId), false);
            return userCoinRepository.save(el);
        }).orElseGet(()->{
            kafkaProducer.sendMessage("coin-topic", new UserCoinLogVO(userId, userCoinVO.balance(), "charge", LocalDateTime.now(), userId), false);
            return userCoinRepository.save(new UserCoin(userId, userCoinVO.balance()));
        });
    }


    @Transactional
    //@CacheEvict(value = "coin", key = "#userId")
    public void updateCoin(final Long userId, final UserCoinVO userCoinVO) {

        stringRedisTemplate.delete("coin:" + userId);
        userCoinRepository.findByUserId(userCoinVO.userId()).map(el -> {
            el.setBalance(el.getBalance() + userCoinVO.balance());
            kafkaProducer.sendMessage("coin-topic", new UserCoinLogVO(userCoinVO.userId(), userCoinVO.balance(), userCoinVO.coinActionType().getCode(), LocalDateTime.now(), userCoinVO.userId()), false);
            return userCoinRepository.save(el);
        }).orElseGet(() -> {
            kafkaProducer.sendMessage("coin-topic", new UserCoinLogVO(userCoinVO.userId(), userCoinVO.balance(), userCoinVO.coinActionType().getCode(), LocalDateTime.now(), userCoinVO.userId()), false);
            return userCoinRepository.save(new UserCoin(userCoinVO.userId(), userCoinVO.balance()));
        });
    }


}
