package com.local_dating.user_service.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig_JSON {

    @Bean
    public LettuceConnectionFactory redisConnectionFactoryJSON() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("aws-server", 6379));
    }

    @Bean
    public RedisTemplate<String, String> jsonStringRedisTemplate(@Qualifier("redisConnectionFactoryJSON") RedisConnectionFactory redisConnectionFactoryJSON) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactoryJSON);

        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(stringSerializer);

        return template;
    }

    @Bean//("jsonCacheManager")  // JSON String을 사용하는 별도의 CacheManager
    public RedisCacheManager jsonCacheManager(@Qualifier("redisConnectionFactoryJSON") RedisConnectionFactory redisConnectionFactoryJSON) {
        // JSON String 직렬화 캐시 매니저
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));

        return RedisCacheManager.builder(redisConnectionFactoryJSON)
                .cacheDefaults(cacheConfig)
                .build();
    }
}
