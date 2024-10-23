package com.local_dating.user_service.application;

import com.local_dating.user_service.domain.vo.UserLoginLogVO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(final KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // 동적으로 토픽을 지정하여 메시지 전송
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }

    public void sentLoginLog(final String topic, final String message) {
        kafkaTemplate.send(topic, message);
    }

    public void sentLoginLog(final String topic, final UserLoginLogVO userLoginLogVO) {
        kafkaTemplate.send(topic, userLoginLogVO);
    }
}
