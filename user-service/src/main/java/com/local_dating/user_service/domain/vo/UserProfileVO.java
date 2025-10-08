package com.local_dating.user_service.domain.vo;

import java.io.Serializable;

public record UserProfileVO(Long userId, String infoCd, String infoVal) implements Serializable {
}
