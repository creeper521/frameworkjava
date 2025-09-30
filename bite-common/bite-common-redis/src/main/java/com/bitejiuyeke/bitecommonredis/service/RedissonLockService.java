package com.bitejiuyeke.bitecommonredis.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * redisson锁服务
 */
@Slf4j
@RequiredArgsConstructor
public class RedissonLockService {
    /**
     * redis操作数据库
     */
    private final RedissonClient redissonClient;

    /**
     * 获取锁
     * @param lockKey
     * @param expire
     * @return
     */
    public RLock acquire(String lockKey, long expire){
        try {
            final RLock lock = redissonClient.getLock(lockKey);

            lock.lock(expire, TimeUnit.MILLISECONDS);
            return lock;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 释放锁
     * @param lock
     * @return
     */
    public boolean releaseLock(RLock lock){
        if(lock.isHeldByCurrentThread()){
            lock.unlock();
            return true;
        }
        return false;
    }
}

