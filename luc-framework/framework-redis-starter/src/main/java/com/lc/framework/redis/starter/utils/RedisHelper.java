package com.lc.framework.redis.starter.utils;

import com.lc.framework.redis.starter.config.LucRedisAutoConfig;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.lc.framework.core.constants.StringConstants.COLON;
import static com.lc.framework.core.constants.StringConstants.EMPTY_STRING;


/**
 * <pre>
 *     {@link LucRedisAutoConfig}中提供了两种RedisHelper：
 *     1、采用json序列化value的“redisHelper”， 未声明bean的名称时默认注入该实例
 *     2、采用byte序列化value的“sessionRedisHelper”
 *     所有的Redis缓存都需要添加前缀， 默认为spring.application.name, 同时提供指定前缀的方法
 * </pre>
 *
 * @Author : Lu Cheng
 * @Date : 2022/11/20 17:54
 * @Version : 1.0
 */
@SuppressWarnings(value = {"unchecked", "rawtypes", "unused"})
@Slf4j
public final class RedisHelper {

    private final RedisTemplate redisTemplate;

    /**
     * duration, time unit is second
     */
    private long EXPIRE = 60 * 60;

    /**
     * the default key prefix is spring.application.name
     * expire should greater than 0, otherwise, 3600 by default
     */
    private final String DEFAULT_PREFIX;

    public RedisHelper(RedisTemplate redisTemplate, long EXPIRE, String DEFAULT_PREFIX) {
        this.redisTemplate = redisTemplate;
        if (EXPIRE > 0) {
            this.EXPIRE = EXPIRE;
        }
        this.DEFAULT_PREFIX = DEFAULT_PREFIX;
    }

    /**
     * generate a real key with prefix, if the prefix is null then use the {@link RedisHelper#DEFAULT_PREFIX}
     *
     * @author Lu Cheng
     * @create 2023/9/22
     */
    private String generateKey(@Nullable String prefix, String key) {
        if (StringUtils.hasLength(prefix)) {
            return prefix + COLON + key;
        }
        return DEFAULT_PREFIX + COLON + key;
    }

    /**
     * get and expire the key by apply timeout
     *
     * @author Lu Cheng
     * @date 2024/03/20
     * @since redis 6.2.0
     */
    public <T> T getAndExpire(String key, long timeout) {
        return getAndExpire(key, EMPTY_STRING, timeout, TimeUnit.SECONDS);
    }

    public <T> T getAndExpire(String key, long timeout, TimeUnit timeUnit) {
        return getAndExpire(key, EMPTY_STRING, timeout, timeUnit);
    }

    public <T> T getAndExpire(String key, String prefix, long timeout) {
        return getAndExpire(key, prefix, timeout, TimeUnit.SECONDS);
    }

    public <T> T getAndExpire(String key, String prefix, long timeout, TimeUnit timeUnit) {
        ValueOperations<String, T> operations = redisTemplate.opsForValue();
        return operations.getAndExpire(generateKey(prefix, key), timeout, timeUnit);
    }

    /**
     * get value of prefixed key
     */
    public <T> T get(String key, String prefix) {
        ValueOperations<String, T> operations = redisTemplate.opsForValue();
        return operations.get(generateKey(prefix, key));
    }

    /**
     * get value of DEFAULT_PREFIX key
     */
    public <T> T get(String key) {
        ValueOperations<String, T> operations = redisTemplate.opsForValue();
        return operations.get(generateKey(EMPTY_STRING, key));
    }

    /**
     * 根据前缀获取所有value
     */
    public <T> List<T> getValues(String pattern) {
        Set<String> keys = this.getKeys(pattern);
        assert keys != null;
        ValueOperations<String, T> operations = redisTemplate.opsForValue();
        return operations.multiGet(keys);
    }

    /**
     * 根据前缀获取所有key
     */
    public Set<String> getKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }


    /**
     * create a key-value cache in redis with default duration as 3600 seconds, and the prefix is {@link RedisHelper#DEFAULT_PREFIX}
     */
    public <T> void set(String key, T value) {
        set(key, value, EMPTY_STRING, EXPIRE);
    }

    /**
     * create a key-value cache in redis with default duration as 3600 seconds, and the prefix is {@link RedisHelper#DEFAULT_PREFIX}
     */
    public <T> void set(String key, T value, long timeout) {
        set(key, value, EMPTY_STRING, timeout);
    }

    /**
     * create a key-value cache in redis with default duration as 3600 seconds
     */
    public <T> void set(String key, T value, String prefix) {
        set(key, value, prefix, EXPIRE);
    }

    /**
     * create a key-value cache in redis with durations
     */
    public <T> void set(String key, T value, String prefix, long timeOut) {
        set(key, value, prefix, timeOut, TimeUnit.SECONDS);
    }


    /**
     * 新增key,并设置过期时间与时间单位。如果timeOut < 0， 表示不需要设置过期时间
     */
    public <T> void set(String key, T value, String prefix, long timeOut, TimeUnit timeUnit) {
        ValueOperations<String, T> operations = redisTemplate.opsForValue();
        if (timeOut < 0) {
            operations.set(generateKey(prefix, key), value);
        } else {
            operations.set(generateKey(prefix, key), value, timeOut, timeUnit);
        }
    }

    /**
     * 判断是否有key
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public boolean hasKey(String key, String prefix) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(generateKey(prefix, key)));
    }

    /**
     * 使key过期
     */
    public void expired(String key) {
        redisTemplate.delete(generateKey(EMPTY_STRING, key));
    }

    /**
     * 使key过期
     */
    public void expired(String key, String prefix) {
        redisTemplate.delete(generateKey(prefix, key));
    }


    /**
     *
     * @param key key
     * @param prefix key前缀
     * @param timeout 过期时间，单位秒
     */
    public <T> T expired(String key, String prefix, long timeout) {
        ValueOperations<String, T> operations = redisTemplate.opsForValue();
        String gkey = generateKey(prefix, key);
        T result = operations.get(gkey);
        redisTemplate.expire(generateKey(prefix, key), timeout, TimeUnit.SECONDS);
        return result;
    }

    /**
     * 批量过期key
     */
    public void expiredAll(String pattern) {
        Set<String> keySet = redisTemplate.keys(pattern);
        if (!CollectionUtils.isEmpty(keySet)) {
            redisTemplate.delete(keySet);
        }
    }


    public Long getAndIncrement(String key, Integer incr) {
        key = generateKey(EMPTY_STRING, key);
        if (hasKey(key)) {
            redisTemplate.opsForValue().set(key, 1);
            return 1L;
        }
        return redisTemplate.opsForValue().increment(key, incr);
    }

    public <T> void hPut(String key, String hashKey, T obj) {
        redisTemplate.opsForHash().put(key, hashKey, obj);
    }

    public <T> T hGet(String key, String hashKey) {
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(key, hashKey);
    }

    /**
     * 向set中添加元素
     */
    public <T> void putSet(String key, T obj) {
        redisTemplate.opsForSet().add(generateKey(EMPTY_STRING, key), obj);
    }

    /**
     * 从set中删除元素
     */
    public <T> void popSet(String key, T obj) {
        redisTemplate.opsForSet().remove(generateKey(EMPTY_STRING, key), obj);
    }

    public Long getSetSize(String key) {
        return redisTemplate.opsForSet().size(generateKey(EMPTY_STRING, key));
    }

    /**
     * 刷新key过期时间
     */
    public <T> void update(String key, T obj, long timeOut) throws Exception {
        if (hasKey(generateKey(EMPTY_STRING, key))) {
            throw new Exception("redis key:" + generateKey(EMPTY_STRING, key) + " 不存在");
        }
        redisTemplate.opsForValue().set(generateKey(EMPTY_STRING, key), obj, timeOut, TimeUnit.SECONDS);
    }




}
