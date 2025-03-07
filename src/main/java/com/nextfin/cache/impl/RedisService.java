package com.nextfin.cache.impl;

import com.nextfin.cache.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class RedisService implements CacheService {


    @Override
    public <T> T getOrFetch(String key, Supplier<T> fetchSupplier) {
        return null;
    }

    @Override
    public <T> T get(String key) {
        return null;
    }

    @Override
    public void set(String key, Object value) {

    }

    @Override
    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {

    }

    @Override
    public <T> T getHashField(String key, String field) {
        return null;
    }

    @Override
    public <T> void setHashField(String key, String field, T value, long timeout, TimeUnit timeUnit) {

    }

    @Override
    public <T> void setHashField(String key, String field, T value) {

    }

    @Override
    public void deleteKey(String key) {

    }
}
