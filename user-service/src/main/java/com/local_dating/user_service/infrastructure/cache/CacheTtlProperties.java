package com.local_dating.user_service.infrastructure.cache;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "cache.ttl")
@Getter
public class CacheTtlProperties {
    private final long refreshTokenTTL;

    public CacheTtlProperties(long refreshToken) {
        this.refreshTokenTTL = refreshToken;
    }
}

/*
@Component
@ConfigurationProperties(prefix = "cache.ttl")
@ConstructorBinding
public class CacheTtlProperties {

    private final int refreshToken;

    public CacheTtlProperties(int refreshToken) {
        this.refreshToken = refreshToken;
    }
}
*/