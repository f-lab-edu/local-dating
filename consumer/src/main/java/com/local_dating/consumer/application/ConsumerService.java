package com.local_dating.consumer.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.local_dating.consumer.domain.entity.UserCoinLog;
import com.local_dating.consumer.domain.entity.UserLoginLog;
import com.local_dating.consumer.domain.mapper.UserLoginLogMapper;
import com.local_dating.consumer.domain.vo.UserCoinLogVO;
import com.local_dating.consumer.domain.vo.UserLoginLogVO;
import com.local_dating.consumer.infrastructure.repository.UserCoinLogRepository;
import com.local_dating.consumer.infrastructure.repository.UserLoginLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class ConsumerService {

    private final UserLoginLogRepository userLoginLogRepository;
    private final UserCoinLogRepository userCoinLogRepository;
    private final UserLoginLogMapper userLoginLogMapper;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    public ConsumerService(UserLoginLogRepository userLoginLogRepository, UserCoinLogRepository userCoinLogRepository, UserLoginLogMapper userLoginLogMapper, ObjectMapper objectMapper) {
        this.userLoginLogRepository = userLoginLogRepository;
        this.userCoinLogRepository = userCoinLogRepository;
        this.userLoginLogMapper = userLoginLogMapper;
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new ParameterNamesModule()).findAndRegisterModules();
    }

    //@RetryableTopic(backoff = @Backoff(delay = 5000, multiplier = 2) /* 5초 이후 2배 증가*/, dltStrategy = DltStrategy.FAIL_ON_ERROR, listenerContainerFactory = )
    @KafkaListener(topics = {"login-log-topic"}, groupId = "my-group")
    //@KafkaListener(topics = {"my-topic"}, groupId = "my-group")
    //@KafkaListener(topics = {"login-log-topic", "login-failure-topic"}, groupId = "log-group")
    public void consume(String message) {
    //public void consume(UserLoginLogVO userLoginLogVO) {
    //public void consume(String message) {
        logger.debug("컨슈머 리스너 login-log-topic : " + message);
        UserLoginLog userLoginLog;
        try {
            userLoginLog = new UserLoginLog(objectMapper.readValue(message, UserLoginLogVO.class));
        } catch (JsonProcessingException e) {
            logger.error("카프카 Message conversion failed: {}", message, e);
            throw new RuntimeException("카프카 Message conversion failed", e);
        }

        userLoginLogRepository.save(userLoginLog);
    }

    @KafkaListener(topics = {"coin-topic"}, groupId = "my-group")
    public void consume2(String message) {
        logger.debug("컨슈머 리스너 coin-topic : " + message);
        UserCoinLog userCoinLog;
        try {
            userCoinLog = new UserCoinLog(objectMapper.readValue(message, UserCoinLogVO.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        userCoinLogRepository.save(userCoinLog);
    }


}
