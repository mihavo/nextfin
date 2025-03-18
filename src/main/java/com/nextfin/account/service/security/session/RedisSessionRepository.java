package com.nextfin.account.service.security.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;

@Configuration
public class RedisSessionRepository {

    @Bean
    public RedisIndexedSessionRepository cacheSessionRepository(RedisTemplate<String, Object> redisTemplate) {
        return new RedisIndexedSessionRepository(redisTemplate);
    }

}
