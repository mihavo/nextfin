package com.nextfin.service.cache;

import com.nextfin.cache.impl.RedisService;
import com.nextfin.config.cache.RedisConfig;
import com.nextfin.exceptions.exception.CacheDisabledException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestRedisService {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private RedisConfig redisConfig;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @InjectMocks
    private RedisService cacheService;


    private static final long DEFAULT_TIMEOUT = 0L;
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;

    @BeforeEach
    public void setup() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        lenient().when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    }

    private void configureCachingEnabled(boolean isEnabled) {
        when(redisConfig.isCachingEnabled()).thenReturn(isEnabled);
    }

    @Test
    public void getOrFetch_cacheHit_returnsValue() {
        configureCachingEnabled(true);
        when(valueOperations.get("key")).thenReturn("cachedValue");
        Supplier<String> supplier = mock(Supplier.class);
        Optional<String> result = cacheService.getOrFetch("key", String.class, supplier);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("cachedValue", result.get());
        verify(valueOperations).get("key");
        verify(supplier, never()).get();
        verify(valueOperations, never()).set(anyString(), any(), anyLong(), any());
        verify(valueOperations, never()).set(anyString(), any());
    }


    @Test
    public void getOrFetch_cacheMiss_returnsValue() {
        configureCachingEnabled(true);
        when(valueOperations.get("key")).thenReturn(null);
        Supplier<String> supplier = () -> "fetched value";
        Optional<String> result = cacheService.getOrFetch("key", String.class, supplier);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("fetched value", result.get());
        verify(valueOperations).get("key");
        verify(valueOperations).set("key", "fetched value", DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT);
    }

    @Test
    public void get_cachingEnabled_returnsValue() {
        configureCachingEnabled(true);
        when(valueOperations.get("key")).thenReturn("cachedValue");
        Optional<String> result = cacheService.get("key", String.class);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("cachedValue", result.get());
        verify(valueOperations).get("key");
        verifyNoMoreInteractions(valueOperations);
    }

    @Test
    public void set_withTimeout_cachingEnabled_setsValue() {
        configureCachingEnabled(true);

        cacheService.set("key", "value", 5, TimeUnit.HOURS);
        verify(valueOperations).set("key", "value", 5, TimeUnit.HOURS);
        verifyNoMoreInteractions(valueOperations);
    }

    @Test
    public void set_defaultTimeout_cachingEnabled_setsValue() {
        configureCachingEnabled(true);

        cacheService.set("key", "value");
        verify(valueOperations).set("key", "value", DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT);
    }

    @Test
    public void getHashField_cachingEnabled_returnsValue() {
        configureCachingEnabled(true);
        when(hashOperations.get("hashKey", "field")).thenReturn("hashValue");

        Optional<String> result = cacheService.getHashField("hashKey", "field", String.class);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("hashValue", result.get());
        verify(hashOperations).get("hashKey", "field");
        verifyNoMoreInteractions(hashOperations);
    }

    @Test
    public void setHashField_withTimeout_cachingEnabled_setsValue() {
        configureCachingEnabled(true);

        cacheService.setHashField("hashKey", "field", "value", 1, TimeUnit.DAYS);

        verify(hashOperations).put("hashKey", "field", "value");
        verify(redisTemplate).expire("hashKey", 1, TimeUnit.DAYS);
        verifyNoMoreInteractions(hashOperations);
    }

    @Test
    public void setHashField_defaultTimeout_cachingEnabled_setsValue() {
        configureCachingEnabled(true);

        cacheService.setHashField("hashKey", "field", "value");

        verify(hashOperations).put("hashKey", "field", "value");
        verify(redisTemplate).expire("hashKey", DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT);
        verifyNoMoreInteractions(hashOperations);
    }

    @Test
    public void delete_cachingEnabled_deletesKey() {
        configureCachingEnabled(true);

        cacheService.deleteKey("key");
        verify(hashOperations).delete("key");
        verifyNoMoreInteractions(hashOperations);
    }

    // --- Tests for Caching Disabled ---

    @Test
    public void getOrFetch_cachingDisabled_throwsException() {
        configureCachingEnabled(false);
        Supplier<String> supplier = mock(Supplier.class);
        Optional<String> result = cacheService.getOrFetch("key", String.class, supplier);

        Assertions.assertTrue(result.isEmpty());
        verifyNoMoreInteractions(redisTemplate);
    }

    @Test
    public void get_cachingDisabled_throwsException() {
        configureCachingEnabled(false);

        Optional<String> result = cacheService.get("key", String.class);

        Assertions.assertTrue(result.isEmpty());
        verifyNoMoreInteractions(redisTemplate);
    }

    @Test
    public void set_withTimeout_cachingDisabled_throwsException() {
        configureCachingEnabled(false);

        CacheDisabledException exc = assertThrows(CacheDisabledException.class, () ->
                cacheService.set("key", "value", 5, TimeUnit.HOURS)
        );
        verifyNoMoreInteractions(redisTemplate);
    }

    @Test
    public void set_defaultTimeout_cachingDisabled_throwsException() {
        configureCachingEnabled(false);

        CacheDisabledException exc = assertThrows(CacheDisabledException.class, () ->
                cacheService.set("key", "value")
        );

        verifyNoMoreInteractions(redisTemplate);
    }

    @Test
    public void getHashField_cachingDisabled_throwsException() {
        configureCachingEnabled(false);

        Optional<String> result = cacheService.getHashField("hashKey", "field", String.class);

        Assertions.assertTrue(result.isEmpty());
        verifyNoMoreInteractions(redisTemplate);
    }

    @Test
    public void setHashField_withTimeout_cachingDisabled_throwsException() {
        configureCachingEnabled(false);

        CacheDisabledException exc = assertThrows(CacheDisabledException.class, () ->
                cacheService.setHashField("hashKey", "field", "value", 1, TimeUnit.DAYS)
        );

        verifyNoMoreInteractions(redisTemplate);
    }

    @Test
    public void setHashField_defaultTimeout_cachingDisabled_throwsException() {
        configureCachingEnabled(false);

        CacheDisabledException exc = assertThrows(CacheDisabledException.class, () ->
                cacheService.setHashField("hashKey", "field", "value")
        );

        verifyNoMoreInteractions(redisTemplate);
    }

    @Test
    public void delete_cachingDisabled_throwsException() {
        configureCachingEnabled(false);

        CacheDisabledException exc = assertThrows(CacheDisabledException.class, () ->
                cacheService.deleteKey("key")
        );

        verifyNoMoreInteractions(redisTemplate);
    }
}
