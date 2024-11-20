package com.local_dating.user_service.presentation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.local_dating.user_service.application.KafkaProducer;
import com.local_dating.user_service.application.UserCoinService;
import com.local_dating.user_service.domain.mapper.UserCoinMapper;
import com.local_dating.user_service.domain.vo.UserCoinLogVO;
import com.local_dating.user_service.presentation.dto.UserCoinDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class CoinController {

    private final UserCoinService userCoinService;
    private final UserCoinMapper userCoinMapper;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    @GetMapping(value = "/v1/users/coin")
    public Long viewCoin(final Authentication authentication) {
        return userCoinService.viewCoin(authentication.getPrincipal().toString());
    }

    @PostMapping(value = "/v1/users/coin")
    public void saveCoin(final Authentication authentication, final UserCoinDTO userCoinDTO) {
        String userId = authentication.getPrincipal().toString();
        userCoinService.saveCoin(userId, userCoinMapper.INSTANCE.toUserCoinVO(userCoinDTO));
        try {
            kafkaProducer.sentLoginLog("coin-topic", objectMapper.writeValueAsString(new UserCoinLogVO(userId, userCoinDTO.balance(), "charge", LocalDateTime.now(),userId)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
