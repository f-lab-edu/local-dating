package com.local_dating.matching_service.domain;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cache.keys")
public record RedisKeys(String userSearchCurrent) {
}
