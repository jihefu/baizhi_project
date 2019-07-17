package com.xxx;

import com.xxx.commons.HostAndPort;

import java.io.IOException;
import java.util.List;

/**
 * 服务端：注册服务 服务名-接口信息 IP和端口信息
 * 消费端：
 *       1、根据接口信息获取服务列表
 *       2、自动更新服务列表
 * /rpc/接口信息/providers/[host:port,...]
 */
public interface Registry {
    String PREFIX="/rpc";
    String SUFFIX="/providers";
    public void register(Class<?> targetInterface,HostAndPort hostAndPort);
    public List<HostAndPort> retriveService(Class<?> targetInterface);
    public void subscribeService(Class<?> targetInterface,List<HostAndPort> hostAndPorts);
    public void close();
}
