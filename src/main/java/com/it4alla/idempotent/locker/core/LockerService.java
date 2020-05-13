package com.it4alla.idempotent.locker.core;

import java.util.concurrent.TimeUnit;
import org.apache.zookeeper.KeeperException;

/**
 * @description: Jedis utils
 *
 * @author
 * @since 1.0.0
 */
public interface LockerService<T> {

    Object lock(String lockKey) throws KeeperException, InterruptedException;

    Object lock(String lockKey,Integer expireTime, TimeUnit timeUnit);

    Object fairLock(String lockKey,Integer expireTime, TimeUnit timeUnit);

    Object tryLock(String lockKey,Integer expireTime, TimeUnit timeUnit,Integer waitTime);

    Object multiLock(Integer expireTime, TimeUnit timeUnit,String ...lockKey);

    void unLock(String lockKey);

    void unLock(Object lock);

    void unLockMultiLock(Object lock);


}
