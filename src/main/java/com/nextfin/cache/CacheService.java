package com.nextfin.cache;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public interface CacheService {

    /**
     * Gets a value from the cache if found, otherwise uses the supplier to fetch it
     *
     * @param key
     * @param fetchSupplier
     * @param valueType
     * @param <T>
     * @return
     */
    <T> Optional<T> getOrFetch(String key, Class<T> valueType, Supplier<T> fetchSupplier);

    /**
     * Retrieve a value from the cache
     *
     * @param key
     * @param valueType
     * @param <T>
     * @return
     */
    <T> Optional<T> get(String key, Class<T> valueType);

    /**
     * Sets a key-value pair in the cache with the default timeout & timeunit
     *
     * @param key
     * @param value
     */
    void set(String key, Object value);

    /**
     * Sets a key-value pair in the cache
     *
     * @param key
     * @param value
     * @param timeout
     * @param timeUnit
     */
    void set(String key, Object value, long timeout, TimeUnit timeUnit);

    /**
     * Retrieves a field value from a hash
     *
     * @param key
     * @param field
     * @param valueType
     * @param <T>
     * @return
     */
    <T> Optional<T> getHashField(String key, String field, Class<T> valueType);

    /**
     * Sets a field-value pair in a hash.
     *
     * @param key
     * @param field
     * @param value
     * @param timeout
     * @param timeUnit
     * @param <T>
     */
    <T> void setHashField(String key, String field, T value, long timeout, TimeUnit timeUnit);

    /**
     * Sets a field-value pair in a hash with the default timeout & timeunit.
     *
     * @param key
     * @param field
     * @param value
     * @param <T>
     */
    <T> void setHashField(String key, String field, T value);

    /**
     * Deletes a key from the cache
     *
     * @param key
     */
    void deleteKey(String key);
}
