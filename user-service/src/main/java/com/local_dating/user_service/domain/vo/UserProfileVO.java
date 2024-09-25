package com.local_dating.user_service.domain.vo;

import java.io.Serializable;

public record UserProfileVO(String userId, String infoCd, String infoVal) implements Serializable {
}
