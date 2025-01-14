package com.local_dating.user_service.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.local_dating.user_service.application.KafkaProducer;
import com.local_dating.user_service.application.UserCoinService;
import com.local_dating.user_service.domain.mapper.UserCoinMapper;
import com.local_dating.user_service.presentation.dto.UserCoinDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CoinController {

    private final UserCoinService userCoinService;
    private final UserCoinMapper userCoinMapper;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(CoinController.class);

    @PreAuthorize("isAuthenticated() and #id == authentication.getPrincipal()")
    @GetMapping(value = "/v1/users/{id}/coin")
    public Long viewCoin(final @PathVariable("id") long id, final Authentication authentication) {
        return userCoinService.viewCoin(authentication.getPrincipal().toString());
    }

    @PreAuthorize("isAuthenticated() and #id == authentication.getPrincipal()")
    @PostMapping(value = "/v1/users/{id}/coin")
    public void saveCoin(final @PathVariable("id") long id, final Authentication authentication, final UserCoinDTO userCoinDTO) {
        String userId = authentication.getPrincipal().toString();
        userCoinService.saveCoin(userId, userCoinMapper.INSTANCE.toUserCoinVO(userCoinDTO));
        /*try {
            logger.info("CoinController 카프카 부분 try");
            kafkaProducer.sentKafkaMsg("coin-topic", objectMapper.writeValueAsString(new UserCoinLogVO(userId, userCoinDTO.balance(), "charge", LocalDateTime.now(),userId)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } finally {
            logger.debug("CoinController 카프카 부분 finally");
        }*/
    }

}
