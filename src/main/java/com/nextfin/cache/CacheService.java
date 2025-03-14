package com.nextfin.cache;

import java.util.Optional;
import java.util.Set;
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
     * Gets a hash value from the cache if found, otherwise uses the supplier to fetch it
     *
     * @param key
     * @param field
     * @param valueType
     * @param fetchSupplier
     * @param <T>
     * @return
     */
    <T> Optional<T> getOrFetchHashField(String key, String field, Class<T> valueType, Supplier<T> fetchSupplier);

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
     * Retrieves all fields from a hash
     *
     * @param key
     * @param valueType
     * @param <T>
     * @return
     */
    <T> Optional<T> getAllFieldsFromHash(String key, Class<T> valueType);

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
     * Saves an object to the cache where each field-value pair corresponds to the key and value of each field
     * * of the object respectively, expiration with custom timeout & timeunit
     *
     * @param key
     * @param object
     * @param timeout
     * @param timeUnit
     * @param <T>
     */
    <T> void setHashObject(String key, T object, long timeout, TimeUnit timeUnit);

    /**
     * Saves an object to the cache where each field-value pair corresponds to the key and value of each field
     * of the object respectively.
     *
     * @param key
     * @param object
     * @param <T>
     */
    <T> void setHashObject(String key, T object);


    /**
     * Gets a range of members of a specified sorted set based on the set key
     *
     * @param setKey
     * @param page
     * @param pageSize
     * @return
     */
    Set<String> getFromSortedSet(String setKey, long page, long pageSize);

    /**
     * Adds an element to a sorted set with a score
     *
     * @param setKey
     * @param member
     * @param score
     */
    void addToSortedSet(String setKey, String member, double score);


    /**
     * Deletes specified members from a sorted set defined by setKey
     *
     * @param setKey
     * @param members
     */
    void deleteFromSortedSet(String setKey, String... members);

    /**
     * Deletes a key from the cache
     *
     * @param key
     */
    void deleteKey(String key);
}
