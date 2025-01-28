package com.local_dating.user_service.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaProducer(final KafkaTemplate<String, Object> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    // 동적으로 토픽을 지정하여 메시지 전송
    public <T> void sendMessage(final String topic, final T vo, boolean isThrow) { // isThrow true 시 Exception 발생

        try {
            kafkaTemplate.send(topic, objectMapper.writeValueAsString(vo));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            if (isThrow) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sentKafkaMsg(final String topic, final String message) {
        kafkaTemplate.send(topic, message);
    }

}
