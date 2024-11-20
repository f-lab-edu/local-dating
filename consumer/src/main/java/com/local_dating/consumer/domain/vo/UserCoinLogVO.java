package com.local_dating.consumer.domain.vo;

import java.time.LocalDateTime;

public record UserCoinLogVO(String userId, Long diff, String action, LocalDateTime localDateTime, String inUserId) {
}
