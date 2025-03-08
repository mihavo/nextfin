package com.nextfin.cache.impl;

import com.nextfin.cache.CacheService;
import com.nextfin.config.cache.RedisConfig;
import com.nextfin.exceptions.exception.CacheDisabledException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class RedisService implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final RedisConfig redisConfig;

    @Value("${nextfin.cache.default-timeout:10}")
    private long defaultTimeout; //TTL in minutes


    @Override
    public <T> Optional<T> getOrFetch(String key, Class<T> valueType, Supplier<T> fetchSupplier) {
        if (!redisConfig.isCachingEnabled()) return Optional.empty();
        Optional<T> result = get(key, valueType);
        if (result.isPresent()) {
            return result;
        }
        result = Optional.of(fetchSupplier.get());
        set(key, result.get());
        return result;
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> valueType) {
        if (!redisConfig.isCachingEnabled()) return Optional.empty();
        Object result = redisTemplate.opsForValue().get(key);
        if (valueType.isInstance(result)) {
            return Optional.of((valueType.cast(result)));
        }
        return Optional.empty();
    }

    @Override
    public void set(String key, Object value) {
        if (!redisConfig.isCachingEnabled()) throw new CacheDisabledException();
        redisTemplate.opsForValue().set(key, value, defaultTimeout, TimeUnit.MINUTES);
    }

    @Override
    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        if (!redisConfig.isCachingEnabled()) throw new CacheDisabledException();
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    @Override
    public <T> Optional<T> getHashField(String key, String field, Class<T> valueType) {
        if (!redisConfig.isCachingEnabled()) return Optional.empty();
        Object result = redisTemplate.opsForHash().get(key, field);
        if (valueType.isInstance(result)) {
            return Optional.of((valueType.cast(result)));
        }
        return Optional.empty();
    }

    @Override
    public <T> void setHashField(String key, String field, T value, long timeout, TimeUnit timeUnit) {
        if (!redisConfig.isCachingEnabled()) throw new CacheDisabledException();
        redisTemplate.opsForHash().put(key, field, value);
        redisTemplate.expire(key, timeout, timeUnit);
    }

    @Override
    public <T> void setHashField(String key, String field, T value) {
        if (!redisConfig.isCachingEnabled()) throw new CacheDisabledException();
        redisTemplate.opsForHash().put(key, field, value);
        redisTemplate.expire(key, defaultTimeout, TimeUnit.MINUTES);
    }

    @Override
    public void deleteKey(String key) {
        if (!redisConfig.isCachingEnabled()) throw new CacheDisabledException();
        redisTemplate.opsForHash().delete(key);
    }

}
