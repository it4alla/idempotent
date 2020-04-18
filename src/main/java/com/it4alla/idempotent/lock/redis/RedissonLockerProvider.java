package com.it4alla.idempotent.lock.redis;

import com.it4alla.idempotent.lock.core.LockerProvider;
import com.it4alla.idempotent.lock.core.LockerService;

/**
 * @description: RedissonLockerProvider
 *
 * @author ITyunqing
 * @since 1.0.0
 */
public class RedissonLockerProvider implements LockerProvider {

    @Override
    public LockerService provider() {
        return RedissonDistributedLocker.getInstance();
    }
}
