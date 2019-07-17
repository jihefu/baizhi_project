package com.xxx.impl;

import com.xxx.commons.HostAndPort;
import com.xxx.Registry;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Created by Administrator on 2018/4/8.
 */
public class ZookeeperReigstry implements Registry {
    private ZkClient client;
    public ZookeeperReigstry (String servers){
        client=new ZkClient(servers);
    }
    public void register(Class<?> targetInterface, HostAndPort hostAndPort) {
        String basePath=PREFIX+"/"+targetInterface.getName()+SUFFIX;
        if(!client.exists(basePath)){
            client.createPersistent(basePath,true);
        }
        String enode=basePath+"/"+hostAndPort.getHost()+":"+hostAndPort.getPort();
        if(client.exists(enode)){
            client.delete(enode);
        }
        client.createEphemeral(enode);
    }

    public List<HostAndPort> retriveService(Class<?> targetInterface) {
        String basePath=PREFIX+"/"+targetInterface.getName()+SUFFIX;
        List<HostAndPort> hostAndPorts=new Vector<HostAndPort>();
        for (String enode : client.getChildren(basePath)) {
            String host=enode.split(":")[0];
            int port=Integer.parseInt(enode.split(":")[1]);
            hostAndPorts.add(new HostAndPort(host,port));
        }
        return  hostAndPorts;
    }

    public void subscribeService(Class<?> targetInterface,final List<HostAndPort> hostAndPorts){
        String basePath=PREFIX+"/"+targetInterface.getName()+SUFFIX;
        client.subscribeChildChanges(basePath, new IZkChildListener() {
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println("服务列表更新...");
                hostAndPorts.clear();//更新服务列表
                for (String enode : currentChilds) {
                    String host=enode.split(":")[0];
                    int port=Integer.parseInt(enode.split(":")[1]);
                    hostAndPorts.add(new HostAndPort(host,port));
                }
            }
        });

    }

    public void close() {
        client.close();
    }
}
