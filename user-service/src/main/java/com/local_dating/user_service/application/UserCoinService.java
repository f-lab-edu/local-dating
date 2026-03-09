package com.local_dating.user_service.application;

import com.local_dating.user_service.domain.KafkaTopics;
import com.local_dating.user_service.domain.RedisKeys;
import com.local_dating.user_service.domain.entity.UserCoin;
import com.local_dating.user_service.domain.mapper.UserCoinMapper;
import com.local_dating.user_service.domain.type.CoinActionType;
import com.local_dating.user_service.domain.vo.UserCoinLogVO;
import com.local_dating.user_service.domain.vo.UserCoinVO;
import com.local_dating.user_service.infrastructure.repository.PricingPolicyRepository;
import com.local_dating.user_service.infrastructure.repository.UserCoinRepository;
import com.local_dating.user_service.util.MessageCode;
import com.local_dating.user_service.util.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCoinService {

    private final UserCoinRepository userCoinRepository;
    private final PricingPolicyRepository pricingPolicyRepository;
    private final UserCoinMapper userCoinMapper;
    private final KafkaProducer kafkaProducer;
    private final KafkaTopics topics;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisKeys keys;

    //@Cacheable(value = "coin", key = "#userId")
    //@Cacheable(value = "coin", key = "#userId", cacheManager = "jsonCacheManager")
    public Long viewCoin(final Long userId) {

        String key = keys.coin() + userId;
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
    public void saveNewCoinData(final Long userId) {
        userCoinRepository.save(new UserCoin(userId, 0L));
    }

    @Transactional
    //@CacheEvict(value = "coin", key = "#userId")
    public void saveCoin(final Long userId, final UserCoinVO userCoinVO) {

        stringRedisTemplate.delete(keys.coin() + userId);
        userCoinRepository.findByUserId(userId).map(el -> {
            el.setBalance(el.getBalance() + userCoinVO.balance());
            kafkaProducer.sendMessage(topics.coin(), new UserCoinLogVO(userId, userCoinVO.balance(), CoinActionType.CHARGE.getCode(), LocalDateTime.now(), userId), false);
            return userCoinRepository.save(el);
        }).orElseGet(()->{
            kafkaProducer.sendMessage(topics.coin(), new UserCoinLogVO(userId, userCoinVO.balance(), CoinActionType.CHARGE.getCode(), LocalDateTime.now(), userId), false);
            return userCoinRepository.save(new UserCoin(userId, userCoinVO.balance()));
        });
    }

    @Transactional
    //@CacheEvict(value = "coin", key = "#userId")
    public void updateCoin(final Long userId, final UserCoinVO userCoinVO) {

        try {
            Thread.sleep(5000);
            log.error("try 5000");
        } catch (Exception exception) {
            log.error("catch 5000");
        }

        stringRedisTemplate.delete(keys.coin() + userId);

        UserCoin userCoin = userCoinRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(MessageCode.DATA_NOT_FOUND_EXCEPTION));

        userCoin.setBalance(userCoin.getBalance() + userCoinVO.balance());

        kafkaProducer.sendMessage(topics.coin(), new UserCoinLogVO(userId, userCoinVO.balance(), userCoinVO.coinActionType().getCode(), LocalDateTime.now(), userCoinVO.userId()), false);

        /*
        userCoinRepository.findByUserId(userCoinVO.userId()).map(el -> {
            el.setBalance(el.getBalance() + userCoinVO.balance());
            kafkaProducer.sendMessage("coin-topic", new UserCoinLogVO(userCoinVO.userId(), userCoinVO.balance(), userCoinVO.coinActionType().getCode(), LocalDateTime.now(), userCoinVO.userId()), false);
            return userCoinRepository.save(el);
        }).orElseGet(() -> {
            kafkaProducer.sendMessage("coin-topic", new UserCoinLogVO(userCoinVO.userId(), userCoinVO.balance(), userCoinVO.coinActionType().getCode(), LocalDateTime.now(), userCoinVO.userId()), false);
            return userCoinRepository.save(new UserCoin(userCoinVO.userId(), userCoinVO.balance()));
        });

         */
    }

    @Transactional
    public void consumeCoin(final Long userId, final UserCoinVO userCoinVO) {

        stringRedisTemplate.delete(keys.coin() + userId);

        UserCoin userCoin = userCoinRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(MessageCode.DATA_NOT_FOUND_EXCEPTION));

        Long price = pricingPolicyRepository.getPricingPolicy("searching").getPrice();
        Long balance = this.viewCoin(userId);

        if (price > balance) throw new BusinessException(MessageCode.INSUFFICIENT_COIN);

        userCoin.setBalance(userCoin.getBalance() - price);

        kafkaProducer.sendMessage(topics.coin(), new UserCoinLogVO(userId, -price, CoinActionType.CONSUME.getCode(), LocalDateTime.now(), userCoinVO.userId()), false);
    }

}
