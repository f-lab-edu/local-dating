package com.local_dating.user_service.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
//@RequiredArgsConstructor
public class UserPreferenceCountVO2 implements Serializable {

    private String userId;
    private Long count;

    public UserPreferenceCountVO2() {

    }

    public UserPreferenceCountVO2(String userId, Long count) {
        this.userId = userId;
        this.count = count;
    }
}
