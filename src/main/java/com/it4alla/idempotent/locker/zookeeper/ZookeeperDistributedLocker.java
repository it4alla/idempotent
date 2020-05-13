package com.it4alla.idempotent.locker.zookeeper;

import com.it4alla.idempotent.locker.core.LockerService;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * @description: Zookeeper Distributed Locker
 *
 * @author ITyunqing
 * @since 1.0.0
 */
public class ZookeeperDistributedLocker implements LockerService {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperDistributedLocker.class);
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();
    private static final String ROOT_NODE_LOCK = "/ROOT_LOCK";
    private static String currentLockId;
    private static final String DATA = "node";
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);

    @Override
    public Object lock(String lockKey) throws KeeperException, InterruptedException {
        ZooKeeper client = new ZookeeperClient().getClient();
        this.checkRootNode(client);
        currentLockId =
                client.create(ROOT_NODE_LOCK+"/",DATA.getBytes(),
                        Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        List<String> children = client.getChildren(ROOT_NODE_LOCK, false);
        if(!CollectionUtils.isEmpty(children)){
            Collections.sort(children);
            String firstNode= children.get(0);
            String currentNode = currentLockId.substring(currentLockId.lastIndexOf("/") + 1);
            threadLocal.set(currentLockId);
            if(currentNode.equals(firstNode)){
                return true;
            }

            int index = children.indexOf(currentNode);
            if(index > 0){
                String preNode = children.get(index - 1);
                client.exists(ROOT_NODE_LOCK + "/" + preNode, new Watcher() {
                    @Override
                    public void process(WatchedEvent watchedEvent) {
                        if(watchedEvent.getType().equals(EventType.NodeDeleted)){
                            countDownLatch.countDown();
                        }
                    }
                });
                countDownLatch.await();
                return true;
            }

        }
        return true;
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
