package com.mq.common.role;

/**
 * Created By xfj on 2020/3/17
 */
public class LoadBalanceMetaInfo extends RoleMetaInfo {
    public LoadBalanceMetaInfo() {
        this.setName("loadbalance");
    }

    public LoadBalanceMetaInfo(String ip, int port, String name) {
        this.ip = ip;
        this.port = port;
        this.name = name;
        this.role="loadbalance";
    }
}
