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
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class ConsumerService {

    private final UserLoginLogRepository userLoginLogRepository;
    private final UserCoinLogRepository userCoinLogRepository;
    private final UserLoginLogMapper userLoginLogMapper;
    private final ObjectMapper objectMapper;

    public ConsumerService(UserLoginLogRepository userLoginLogRepository, UserCoinLogRepository userCoinLogRepository, UserLoginLogMapper userLoginLogMapper, ObjectMapper objectMapper) {
        this.userLoginLogRepository = userLoginLogRepository;
        this.userCoinLogRepository = userCoinLogRepository;
        this.userLoginLogMapper = userLoginLogMapper;
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new ParameterNamesModule()).findAndRegisterModules();
    }

    @KafkaListener(topics = {"my-topic"}, groupId = "my-group")
    //@KafkaListener(topics = {"login-log-topic", "login-failure-topic"}, groupId = "log-group")
    public void consume(String message) {
    //public void consume(UserLoginLogVO userLoginLogVO) {
    //public void consume(String message) {

        UserLoginLog userLoginLog;
        try {
            userLoginLog = new UserLoginLog(objectMapper.readValue(message, UserLoginLogVO.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        userLoginLogRepository.save(userLoginLog);
    }

    @KafkaListener(topics = {"coin-topic"}, groupId = "my-group")
    public void consume2(String message) {
        UserCoinLog userCoinLog;
        try {
            userCoinLog = new UserCoinLog(objectMapper.readValue(message, UserCoinLogVO.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        userCoinLogRepository.save(userCoinLog);
    }
}
