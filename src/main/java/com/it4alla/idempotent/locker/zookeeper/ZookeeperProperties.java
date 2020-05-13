package com.it4alla.idempotent.locker.zookeeper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description: Zookeeper Properties
 *
 * @author ITyunqing
 * @since 1.0.0
 */
@Component
public class ZookeeperProperties {

    @Value("${zk.host}")
    private String zkHost;
    @Value("${zk.session.timeout}")
    private Integer zkSessionTimeout;

    public String getZkHost() {
        return zkHost;
    }

    public void setZkHost(String zkHost) {
        this.zkHost = zkHost;
    }

    public Integer getZkSessionTimeout() {
        return zkSessionTimeout;
    }

    public void setZkSessionTimeout(Integer zkSessionTimeout) {
        this.zkSessionTimeout = zkSessionTimeout;
    }
}
