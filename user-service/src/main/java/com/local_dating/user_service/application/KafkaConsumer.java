package com.local_dating.user_service.application;

import com.local_dating.user_service.domain.mapper.UserLoginLogMapper;
import com.local_dating.user_service.domain.vo.UserLoginLogVO;
import com.local_dating.user_service.infrastructure.repository.UserLoginLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final UserLoginLogRepository userLoginLogRepository;
    private final UserLoginLogMapper userLoginLogMapper;

    @KafkaListener(topics = {"my-topic", "login-log-topic", "login-failure-topic"}, groupId = "my-group")
    //@KafkaListener(topics = {"login-log-topic", "login-failure-topic"}, groupId = "log-group")
    public void consume(UserLoginLogVO userLoginLogVO) {
    //public void consume(String message) {
        //System.out.println("Received Message: " + message);
        // 메시지에 따른 로직 처리

        System.out.println("컨슈머확인: " + userLoginLogVO.userId());

        userLoginLogRepository.save(userLoginLogMapper.toUserLoginLog(userLoginLogVO));
    }
}
