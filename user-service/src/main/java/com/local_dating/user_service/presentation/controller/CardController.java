package com.local_dating.user_service.presentation.controller;

import com.local_dating.user_service.application.UserCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CardController {

    private final UserCardService userCardService;

    @GetMapping(value = "/v1/users/{id}/cards")
    public List viewRecomcard(final Authentication authentication) {
        return userCardService.getCard(authentication.getPrincipal().toString());
    }

    @PostMapping(value = "/v1/users/{id}/cards")
    public void saveRecomcard(final Authentication authentication) {
        userCardService.setCard(authentication.getPrincipal().toString());
    }

    @GetMapping(value = "/v1/users/{id}/cards/detail/{targetId}")
    public List viewRecomcardDetail(final Authentication authentication, @PathVariable String targetId) {
        return userCardService.getCardDetail(authentication.getPrincipal().toString(), targetId);
    }
}
