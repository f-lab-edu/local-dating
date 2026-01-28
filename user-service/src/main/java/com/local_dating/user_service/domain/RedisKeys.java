package com.local_dating.user_service.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cache.keys")
public record RedisKeys(String userRefreshToken, String coin) {
}
