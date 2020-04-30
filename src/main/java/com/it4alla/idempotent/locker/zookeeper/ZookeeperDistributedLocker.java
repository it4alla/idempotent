package com.it4alla.idempotent.locker.zookeeper;

import com.it4alla.idempotent.locker.core.LockerService;
import java.util.concurrent.TimeUnit;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description: Zookeeper Distributed Locker
 *
 * @author ITyunqing
 * @since 1.0.0
 */
public class ZookeeperDistributedLocker implements LockerService {
    private static  final Logger logger = LoggerFactory.getLogger(ZookeeperDistributedLocker.class);
    private static final String ROOT_NODE_LOCK = "/ROOT_LOCK";
    private static String currentLockId;
    private static final String DATA = "node";

    @Override
    public Object lock(String lockKey) {
        ZooKeeper client = new ZookeeperClient().getClient();
        this.checkRootNode(client);
        //TODO
        return null;
    }

    /**
     * check root node
     */
    private void checkRootNode(ZooKeeper client){
        try {
            Stat stat = client.exists(ROOT_NODE_LOCK, false);
            if (null == stat) {
                client.create(ROOT_NODE_LOCK, DATA.getBytes(), Ids.OPEN_ACL_UNSAFE,
                        CreateMode.PERSISTENT);
            }
        }catch (Exception ex){
            logger.info("【Zookeeper】Failed to create root node",ex);
        }


    }

    @Override
    public Object lock(String lockKey, Integer expireTime, TimeUnit timeUnit) {
        //FIXME add not support ex
        return null;
    }

    @Override
    public Object fairLock(String lockKey, Integer expireTime, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public Object tryLock(String lockKey, Integer expireTime, TimeUnit timeUnit, Integer waitTime) {
        return null;
    }

    @Override
    public Object multiLock(Integer expireTime, TimeUnit timeUnit, String... lockKey) {
        return null;
    }

    @Override
    public void unLock(String lockKey) {

    }

    @Override
    public void unLock(Object lock) {

    }

    @Override
    public void unLockMultiLock(Object lock) {

    }
}
