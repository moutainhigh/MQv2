package com.mq.producer.net;

import com.rpc.client.ClientStubProxyFactory;
import com.rpc.client.net.NettyNetClient;
import com.rpc.common.protocol.JavaSerializeMessageProtocol;
import com.rpc.common.protocol.MessageProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * Created By xfj on 2020/3/23
 */
public class RPCManager {
    private  ClientStubProxyFactory cspf;

    public RPCManager(ClientStubProxyFactory cspf) {
        this.cspf = cspf;
    }

    public RPCManager() {
        init();
    }

    public  void init(){
        cspf = new ClientStubProxyFactory();
        // 设置服务发现者
        cspf.setSid(new BrokerServiceDiscover());
        // 设置支持的协议
        Map<String, MessageProtocol> supportMessageProtocols = new HashMap<>();
//        supportMessageProtocols.put("jasetSupportMessageProtocolsvas", new JavaSerializeMessageProtocol());
        supportMessageProtocols.put("javas", new JavaSerializeMessageProtocol());
        cspf.setSupportMessageProtocols(supportMessageProtocols);
        // 设置网络层实现
        cspf.setNetClient(new NettyNetClient());
    }

    public <T> T getRPCProxy(Class<T> clz,String routeKey){
        if(cspf==null){
            init();
        }
        T proxy = cspf.getProxy(clz, routeKey);
        return proxy;
    }
}
