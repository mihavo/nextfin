package com.nextfin.cache.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextfin.cache.CacheService;
import com.nextfin.config.cache.RedisConfig;
import com.nextfin.exceptions.exception.CacheDisabledException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class RedisService implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final StringRedisTemplate stringRedisTemplate;

    private final RedisConfig redisConfig;

    private final ObjectMapper jsonMapper;


    @Value("${nextfin.cache.default-timeout:10}")
    private long defaultTimeout; //TTL in minutes


    @Override
    public <T> Optional<T> getOrFetch(String key, Class<T> valueType, Supplier<T> fetchSupplier) {
        if (!RedisConfig.isCachingEnabled()) return Optional.empty();
        Optional<T> result = get(key, valueType);
        if (result.isPresent()) {
            return result;
        }
        result = Optional.of(fetchSupplier.get());
        set(key, result.get());
        return result;
    }

    @Override
    public <T> Optional<T> getOrFetchHashField(String key, String field, Class<T> valueType,
                                               Supplier<T> fetchSupplier) {
        if (!RedisConfig.isCachingEnabled()) return Optional.empty();
        Optional<T> result = getHashField(key, field, valueType);
        if (result.isPresent()) {
            return result;
        }
        result = Optional.of(fetchSupplier.get());
        set(key, result.get());
        return result;
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> valueType) {
        if (!RedisConfig.isCachingEnabled()) return Optional.empty();
        Object result = redisTemplate.opsForValue().get(key);
        if (valueType.isInstance(result)) {
            return Optional.of((valueType.cast(result)));
        }
        return Optional.empty();
    }

    @Override
    public void set(String key, Object value) {
        if (!RedisConfig.isCachingEnabled()) throw new CacheDisabledException();
        redisTemplate.opsForValue().set(key, value, defaultTimeout, TimeUnit.MINUTES);
    }

    @Override
    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        if (!RedisConfig.isCachingEnabled()) throw new CacheDisabledException();
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    @Override
    public <T> Optional<T> getHashField(String key, String field, Class<T> valueType) {
        if (!RedisConfig.isCachingEnabled()) return Optional.empty();
        Object result = redisTemplate.opsForHash().get(key, field);
        if (valueType.isInstance(result)) {
            return Optional.of((valueType.cast(result)));
        }
        return Optional.empty();
    }

    @Override
    public <T> Optional<T> getAllFieldsFromHash(String key, Class<T> valueType) {
        if (!RedisConfig.isCachingEnabled()) return Optional.empty();
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        if (entries.isEmpty()) return Optional.empty();
        return Optional.of(jsonMapper.convertValue(entries, valueType));
    }

    @Override
    public <T> void setHashField(String key, String field, T value, long timeout, TimeUnit timeUnit) {
        if (!RedisConfig.isCachingEnabled()) throw new CacheDisabledException();
        redisTemplate.opsForHash().put(key, field, value);
            redisTemplate.expire(key, timeout, timeUnit);
    }

    @Override
    public <T> void setHashField(String key, String field, T value) {
        if (!RedisConfig.isCachingEnabled()) throw new CacheDisabledException();
        redisTemplate.opsForHash().put(key, field, value);

        redisTemplate.expire(key, defaultTimeout, TimeUnit.MINUTES);
    }

    @Override
    public <T> void setHashObject(String key, T object, long timeout, TimeUnit timeUnit) {
        if (!RedisConfig.isCachingEnabled()) throw new CacheDisabledException();
        Map<String, Object> fields = jsonMapper.convertValue(object, new TypeReference<>() {
        });
        redisTemplate.opsForHash().putAll(key, fields);
        redisTemplate.expire(key, timeout, timeUnit);
    }

    @Override
    public <T> void setHashObject(String key, T object) {
        if (!RedisConfig.isCachingEnabled()) throw new CacheDisabledException();
        Map<String, Object> fields = jsonMapper.convertValue(object, new TypeReference<>() {
        });
        redisTemplate.opsForHash().putAll(key, fields);
        redisTemplate.expire(key, defaultTimeout, TimeUnit.MINUTES);

    }

    @Override
    public Set<String> getFromSortedSet(String setKey, long page, long pageSize) {
        if (!RedisConfig.isCachingEnabled()) return Set.of();
        long start = (page - 1) * pageSize;
        long end = start + pageSize - 1;
        Set<String> range = stringRedisTemplate.opsForZSet().reverseRange(setKey, start, end);
        if (range == null || range.isEmpty()) {
            return Set.of();
        }
        return range;
    }

    @Override
    public Set<String> getFromSortedSet(String setKey, long page) {
        if (!RedisConfig.isCachingEnabled()) return Set.of();
        Set<String> allEntries = stringRedisTemplate.opsForZSet().reverseRange(setKey, 0, -1);
        return allEntries != null ? allEntries : Set.of();
    }

    @Override
    public void addToSortedSet(String setKey, String member, double score) {
        if (!RedisConfig.isCachingEnabled()) throw new CacheDisabledException();
        stringRedisTemplate.opsForZSet().addIfAbsent(setKey, member, score);
    }

    @Override
    public void deleteFromSortedSet(String setKey, String... members) {
        if (!RedisConfig.isCachingEnabled()) throw new CacheDisabledException();
        stringRedisTemplate.opsForZSet().remove(setKey, (Object[]) members);
    }

    @Override
    public void deleteKey(String key) {
        if (!RedisConfig.isCachingEnabled()) throw new CacheDisabledException();
        redisTemplate.opsForHash().delete(key);
    }

}
