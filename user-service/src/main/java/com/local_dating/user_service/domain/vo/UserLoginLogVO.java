package com.local_dating.user_service.domain.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

public record UserLoginLogVO(String userId, String ip, String lgFailYn, LocalDateTime localDateTime) implements Serializable {
}
