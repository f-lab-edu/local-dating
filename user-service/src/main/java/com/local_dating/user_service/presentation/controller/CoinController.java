package com.local_dating.user_service.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.local_dating.user_service.application.KafkaProducer;
import com.local_dating.user_service.application.UserCoinService;
import com.local_dating.user_service.domain.mapper.UserCoinMapper;
import com.local_dating.user_service.presentation.dto.UserCoinDTO;
import com.local_dating.user_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CoinController {

    private final UserCoinService userCoinService;
    private final UserCoinMapper userCoinMapper;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(CoinController.class);

    @PreAuthorize("isAuthenticated() and #id == authentication.getPrincipal()")
    @GetMapping(value = "/v1/users/{id}/coin")
    public Long viewCoin(final @PathVariable("id") long id, final @RequestHeader("Authorization") String authentication) {
        return userCoinService.viewCoin(id);
        //return userCoinService.viewCoin(jwtUtil.getAuthenticationFromToken(authentication).getPrincipal().toString());
    }

    @PreAuthorize("isAuthenticated() and #id == authentication.getPrincipal()")
    @PostMapping(value = "/v1/users/{id}/coin")
    public void saveCoin(final @PathVariable("id") long id, final @RequestHeader("Authorization") String authentication, @RequestBody final UserCoinDTO userCoinDTO) {

        //String userId = jwtUtil.getAuthenticationFromToken(authentication).getPrincipal().toString();
        userCoinService.saveCoin(id, userCoinMapper.INSTANCE.toUserCoinVO(userCoinDTO));
    }

    @PreAuthorize("isAuthenticated() and #id == authentication.getPrincipal()")
    @PostMapping(value = "/v1/users/{id}/coins")
    public void updateCoin(final @PathVariable("id") long id, final @RequestHeader("Authorization") String authentication, @RequestBody final UserCoinDTO userCoinDTO) {
        userCoinService.updateCoin(id, userCoinMapper.INSTANCE.toUserCoinVO(userCoinDTO));
    }

    /*@PreAuthorize("isAuthenticated() and #id == authentication.getPrincipal()")
    @PostMapping(value = "/v1/users/{id}/coin")
    public void saveCoin(final @PathVariable("id") long id, final Authentication authentication, final UserCoinDTO userCoinDTO) {
        String userId = authentication.getPrincipal().toString();
        userCoinService.saveCoin(userId, userCoinMapper.INSTANCE.toUserCoinVO(userCoinDTO));
    }*/

}
