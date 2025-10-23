package com.bitejiuyeke.bitecommoncache.utils;

import com.bitejiuyeke.bitecommonredis.service.RedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class CacheUtil {
    public static <T> T getL2Cache(RedisService redisService,
                                   String key,
                                   TypeReference<T> typeReference,
                                   Cache<String, Object> cache){
        T res =(T) cache.getIfPresent(key);
        if(res != null){

            log.info("从本地缓存获取数据" + key);
            return res;
        }

        res = redisService.getCacheObject(key, typeReference);

        if(res != null){
            log.info("从redis获取数据" + key);
            cache.put(key, res);
            return res;
        }
        return null;
    }

    /**
     * 设置二级缓存
     * @param redisService
     * @param key
     * @param value
     * @param cache
     * @param timeUnit
     * @param <T>
     */
    public static <T> void setL2Cache(RedisService redisService,
                                      String key,
                                      T value,
                                      Cache<String, Object> cache,
                                      final Long timeout,
                                      final TimeUnit timeUnit){
        redisService.setCacheObject(key, value, timeout, timeUnit);
        log.info("更新redis缓存 key: " + key +" value: "+value);

        cache.put(key, value);
        log.info("更新本地缓存 key: " + key +" value: "+value);
    }
}
