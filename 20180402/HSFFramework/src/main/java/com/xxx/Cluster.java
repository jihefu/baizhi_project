package com.xxx;

import com.xxx.commons.HostAndPort;
import com.xxx.commons.MethodInvokerMetaWrap;
import com.xxx.commons.ResultWrap;

import java.util.List;

/**
 * Created by Administrator on 2018/4/8.
 */
public interface Cluster {
    /**
     *
     * @param hostAndPorts :可调用的服务列表
     * @param loadBalancer ：负载均衡算法
     * @param transfer     : 负责网络传输
     * @param mimw          ：请求参数
     * @return              ：响应参数
     */
    public ResultWrap invoker(List<HostAndPort> hostAndPorts, LoadBalancer loadBalancer,
                              Transfer transfer, MethodInvokerMetaWrap mimw);
}
