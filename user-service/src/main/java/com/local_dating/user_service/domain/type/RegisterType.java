package com.local_dating.user_service.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegisterType {

    MANUAL("일반가입"),
    SOCIAL("소셜가입");

    private final String description;
}
