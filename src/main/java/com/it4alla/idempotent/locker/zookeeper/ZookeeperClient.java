package com.it4alla.idempotent.locker.zookeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @description: Zookeeper Client
 *
 * @author ITyunqing
 * @since 1.0.0
 */
public class ZookeeperClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperClient.class);
    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    @Autowired
    private ZookeeperProperties properties;

    public ZookeeperClient() {
    }

    public ZooKeeper getClient(){
        try {
            ZooKeeper zookeeper = new ZooKeeper(properties.getZkHost(),
                    properties.getZkSessionTimeout(), new ZKWatcher());
            countDownLatch.await();
            return zookeeper;
        } catch (IOException e) {
            e.printStackTrace();
            //FIXME
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            //FIXME
            return null;
        }
    }


    private class ZKWatcher implements Watcher {
        @Override
        public void process(WatchedEvent watchedEvent) {
            if (KeeperState.SyncConnected == watchedEvent.getState()) {
                countDownLatch.countDown();
                LOGGER.info("zookeeper client host:{} connect successed",properties.getZkHost());
            } else {
                LOGGER.info("zookeeper client host:{} connecting ......",properties.getZkHost());
            }
        }
    }
}
