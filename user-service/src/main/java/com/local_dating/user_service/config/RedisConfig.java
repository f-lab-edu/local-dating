package com.local_dating.user_service.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    @Primary
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration("aws-server", 6379));
    }

    //값 직렬화기(단일 인스턴스 공유)
    @Bean
    public RedisSerializer<Object> redisValueSerializer() {
        return RedisSerializer.json(); // GenericJackson2JsonRedisSerializer 기반
    }

    // 범용 RedisTemplate (키: String / 값: JSON)
    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory,
            RedisSerializer<Object> redisValueSerializer
    ) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);
        template.setValueSerializer(redisValueSerializer);
        template.setHashValueSerializer(redisValueSerializer);
        template.setDefaultSerializer(redisValueSerializer);

        template.afterPropertiesSet();
        return template;
    }

    // 4) 범용 CacheManager (키: String / 값: JSON)
    @Bean
    @Primary
    public RedisCacheManager cacheManager(
            RedisConnectionFactory connectionFactory,
            RedisSerializer<Object> redisValueSerializer
    ) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisValueSerializer))
                .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }

    /*@Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory cf) {
        return new StringRedisTemplate(cf);
    }*/

    @Bean
    public RedisTemplate<String, Long> redisStringLongTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, Long> template = new RedisTemplate<>();
        template.setConnectionFactory(cf);

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        GenericToStringSerializer<Long> valueSerializer = new GenericToStringSerializer<>(Long.class);

        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);

        template.afterPropertiesSet();
        return template;
    }


}