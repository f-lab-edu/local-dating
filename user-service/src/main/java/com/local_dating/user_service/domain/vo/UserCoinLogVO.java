package com.local_dating.user_service.domain.vo;

import java.time.LocalDateTime;

public record UserCoinLogVO(Long userId, Long diff, String action, LocalDateTime localDateTime, Long inUserId) {
}
