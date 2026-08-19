package com.local_dating.user_service.presentation.controller;

import com.local_dating.user_service.application.UserCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CardController {

    private final UserCardService userCardService;

    @PreAuthorize("isAuthenticated() and #id == principal.userNo")
    @GetMapping(value = "/api/users/{id}/cards")
    public List viewRecomcard(final @PathVariable("id") long id) {
        return userCardService.getCard(id);
    }

    @PreAuthorize("isAuthenticated() and #id == principal.userNo")
    @PostMapping(value = "/api/users/{id}/cards")
    public void saveRecomcard(final @PathVariable("id") long id) {
        userCardService.setCard(id);
    }

    @PreAuthorize("isAuthenticated() and #id == principal.userNo")
    @GetMapping(value = "/api/users/{id}/cards/detail/{targetId}")
    public List viewRecomcardDetail(final @PathVariable("id") long id, @PathVariable Long targetId) {
        return userCardService.getCardDetail(targetId);
    }
}
