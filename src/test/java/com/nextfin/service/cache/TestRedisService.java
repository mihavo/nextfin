package com.nextfin.service.cache;

import com.nextfin.cache.impl.RedisService;
import com.nextfin.config.cache.RedisConfig;
import com.nextfin.exceptions.exception.CacheDisabledException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.*;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestRedisService {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private RedisConfig redisConfig;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @Mock
    private ZSetOperations<String, String> zSetOperations;

    @InjectMocks
    private RedisService cacheService;


    private final long DEFAULT_TIMEOUT = 0L;
    private final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;

    @BeforeEach
    public void setup() {
        cacheService = new RedisService(redisTemplate, stringRedisTemplate, redisConfig, null);
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        lenient().when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        lenient().when(stringRedisTemplate.opsForZSet()).thenReturn(zSetOperations);
    }

    @AfterEach
    public void teardown() {
        reset(redisTemplate, stringRedisTemplate, redisConfig);
    }

    private MockedStatic<RedisConfig> configureCachingEnabled(boolean isEnabled) {
        MockedStatic<RedisConfig> redisConfig = mockStatic(RedisConfig.class);
        redisConfig.when(RedisConfig::isCachingEnabled).thenReturn(isEnabled);
        return redisConfig;
    }

    @Test
    public void getOrFetch_cacheHit_returnsValue() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(true)) {
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
    }


    @Test
    public void getOrFetch_cacheMiss_returnsValue() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(true)) {
            when(valueOperations.get("key")).thenReturn(null);
            Supplier<String> supplier = () -> "fetched value";
            Optional<String> result = cacheService.getOrFetch("key", String.class, supplier);
            Assertions.assertTrue(result.isPresent());
            Assertions.assertEquals("fetched value", result.get());
            verify(valueOperations).get("key");
            verify(valueOperations).set("key", "fetched value", DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT);
        }
    }


    @Test
    public void getOrFetchHashField_cacheHit_returnsValue() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(true)) {
            when(hashOperations.get("key", "field")).thenReturn("cachedValue");
            Supplier<String> supplier = mock(Supplier.class);
            Optional<String> result = cacheService.getOrFetchHashField("key", "field", String.class, supplier);
            Assertions.assertTrue(result.isPresent());
            Assertions.assertEquals("cachedValue", result.get());
            verify(hashOperations).get("key", "field");
            verify(supplier, never()).get();
            verify(hashOperations, never()).put(anyString(), any(), any());
        }
    }

    @Test
    public void get_cachingEnabled_returnsValue() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(true)) {
            when(valueOperations.get("key")).thenReturn("cachedValue");
            Optional<String> result = cacheService.get("key", String.class);
            Assertions.assertTrue(result.isPresent());
            Assertions.assertEquals("cachedValue", result.get());
            verify(valueOperations).get("key");
            verifyNoMoreInteractions(valueOperations);
        }
    }

    @Test
    public void set_withTimeout_cachingEnabled_setsValue() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(true)) {
            cacheService.set("key", "value", 5, TimeUnit.HOURS);
            verify(valueOperations).set("key", "value", 5, TimeUnit.HOURS);
            verifyNoMoreInteractions(valueOperations);
        }
    }

    @Test
    public void set_defaultTimeout_cachingEnabled_setsValue() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(true)) {
            cacheService.set("key", "value");
            verify(valueOperations).set("key", "value", DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT);
        }
    }

    @Test
    public void getHashField_cachingEnabled_returnsValue() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(true)) {
            when(hashOperations.get("hashKey", "field")).thenReturn("hashValue");

            Optional<String> result = cacheService.getHashField("hashKey", "field", String.class);

            Assertions.assertTrue(result.isPresent());
            Assertions.assertEquals("hashValue", result.get());
            verify(hashOperations).get("hashKey", "field");
            verifyNoMoreInteractions(hashOperations);
        }
    }

    @Test
    public void setHashField_withTimeout_cachingEnabled_setsValue() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(true)) {
            cacheService.setHashField("hashKey", "field", "value", 1, TimeUnit.DAYS);

            verify(hashOperations).put("hashKey", "field", "value");
            verify(redisTemplate).expire("hashKey", 1, TimeUnit.DAYS);
            verifyNoMoreInteractions(hashOperations);
        }
    }

    @Test
    public void setHashField_defaultTimeout_cachingEnabled_setsValue() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(true)) {
            cacheService.setHashField("hashKey", "field", "value");

            verify(hashOperations).put("hashKey", "field", "value");
            verify(redisTemplate).expire("hashKey", DEFAULT_TIMEOUT, DEFAULT_TIME_UNIT);
            verifyNoMoreInteractions(hashOperations);
        }
    }

    @Test
    public void delete_cachingEnabled_deletesKey() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(true)) {
            cacheService.deleteKey("key");
            verify(hashOperations).delete("key");
            verifyNoMoreInteractions(hashOperations);
        }
    }

    @Test
    public void getFromSortedSet_cachingEnabled_returnsValues() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(true)) {
            Set<String> expected = Set.of("txn1", "txn2", "txn3");
            when(stringRedisTemplate.opsForZSet().reverseRange("transactions:account1", 0, 2)).thenReturn(expected);

            Set<String> result = cacheService.getFromSortedSet("transactions:account1", 1, 3);

            Assertions.assertFalse(result.isEmpty());
            Assertions.assertEquals(expected, result);
            verify(stringRedisTemplate.opsForZSet()).reverseRange("transactions:account1", 0, 2);
            verifyNoMoreInteractions(zSetOperations);
        }
    }

    @Test
    public void addToSortedSet_cachingEnabled_addsMember() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(true)) {
            when(stringRedisTemplate.opsForZSet().addIfAbsent("transactions:account1", "txn4", 10.0)).thenReturn(true);

            cacheService.addToSortedSet("transactions:account1", "txn4", 10.0);

            verify(stringRedisTemplate.opsForZSet()).addIfAbsent("transactions:account1", "txn4", 10.0);
            verifyNoMoreInteractions(zSetOperations);
        }
    }

    @Test
    public void deleteFromSortedSet_cachingEnabled_deletesMembers() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(true)) {
            cacheService.deleteFromSortedSet("transactions:account1", "txn1", "txn2");
            verify(stringRedisTemplate.opsForZSet()).remove("transactions:account1", "txn1", "txn2");
            verifyNoMoreInteractions(zSetOperations);
        }
    }

    // --- Tests for Caching Disabled ---

    @Test
    public void getOrFetch_cachingDisabled_throwsException() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(false)) {
            Supplier<String> supplier = mock(Supplier.class);
            Optional<String> result = cacheService.getOrFetch("key", String.class, supplier);

            Assertions.assertTrue(result.isEmpty());
            verifyNoMoreInteractions(redisTemplate);
        }
    }

    @Test
    public void get_cachingDisabled_throwsException() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(false)) {
            Optional<String> result = cacheService.get("key", String.class);

            Assertions.assertTrue(result.isEmpty());
            verifyNoMoreInteractions(redisTemplate);
        }
    }

    @Test
    public void set_withTimeout_cachingDisabled_throwsException() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(false)) {
            assertThrows(CacheDisabledException.class, () -> cacheService.set("key", "value", 5, TimeUnit.HOURS));
            verifyNoMoreInteractions(redisTemplate);
        }
    }

    @Test
    public void set_defaultTimeout_cachingDisabled_throwsException() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(false)) {
            assertThrows(CacheDisabledException.class, () -> cacheService.set("key", "value"));
            verifyNoMoreInteractions(redisTemplate);
        }
    }

    @Test
    public void getHashField_cachingDisabled_throwsException() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(false)) {
            Optional<String> result = cacheService.getHashField("hashKey", "field", String.class);

            Assertions.assertTrue(result.isEmpty());
            verifyNoMoreInteractions(redisTemplate);
        }
    }

    @Test
    public void setHashField_withTimeout_cachingDisabled_throwsException() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(false)) {
            assertThrows(CacheDisabledException.class,
                         () -> cacheService.setHashField("hashKey", "field", "value", 1, TimeUnit.DAYS));

            verifyNoMoreInteractions(redisTemplate);
        }
    }

    @Test
    public void setHashField_defaultTimeout_cachingDisabled_throwsException() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(false)) {
            assertThrows(CacheDisabledException.class, () -> cacheService.setHashField("hashKey", "field", "value"));

            verifyNoMoreInteractions(redisTemplate);
        }
    }

    @Test
    public void delete_cachingDisabled_throwsException() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(false)) {
            assertThrows(CacheDisabledException.class, () -> cacheService.deleteKey("key"));

            verifyNoMoreInteractions(redisTemplate);
        }
    }

    @Test
    public void getFromSortedSet_cachingDisabled_returnsEmptySet() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(false)) {
            Set<String> result = cacheService.getFromSortedSet("transactions:account1", 1, 3);

            Assertions.assertTrue(result.isEmpty());
            verifyNoMoreInteractions(stringRedisTemplate);
        }
    }

    @Test
    public void addToSortedSet_cachingDisabled_throwsCacheDisabledException() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(false)) {
            Assertions.assertThrows(CacheDisabledException.class,
                                    () -> cacheService.addToSortedSet("transactions:account1", "txn4", 10.0));
            verifyNoMoreInteractions(stringRedisTemplate);
        }
    }

    @Test
    public void deleteFromSortedSet_cachingDisabled_throwsCacheDisabledException() {
        try (MockedStatic<RedisConfig> redisConfig = configureCachingEnabled(false)) {
            Assertions.assertThrows(CacheDisabledException.class,
                                    () -> cacheService.deleteFromSortedSet("transactions:account1", "txn1", "txn2"));

            verifyNoMoreInteractions(stringRedisTemplate);
        }
    }
}
