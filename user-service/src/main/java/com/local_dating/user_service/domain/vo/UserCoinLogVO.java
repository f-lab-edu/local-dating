package com.local_dating.user_service.domain.vo;

import java.time.LocalDateTime;

public record UserCoinLogVO(String userId, Long diff, String action, LocalDateTime localDateTime, String inUserId) {
}
