package com.it4alla.idempotent.locker.redis;

import com.it4alla.idempotent.locker.core.LockerProvider;
import com.it4alla.idempotent.locker.core.LockerService;

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
