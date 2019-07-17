package com.xxx.impl;

import com.xxx.*;
import com.xxx.commons.HostAndPort;
import com.xxx.commons.MethodInvokerMetaWrap;
import com.xxx.commons.ResultWrap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Created by Administrator on 2018/4/8.
 */
public class FailoverCluster implements Cluster {
    public ResultWrap invoker(List<HostAndPort> hostAndPorts, LoadBalancer loadBalancer, Transfer transfer, MethodInvokerMetaWrap mimw) {
        List<HostAndPort> newHostPorts= new Vector<HostAndPort>(Arrays.asList(new HostAndPort[hostAndPorts.size()]));
        Collections.copy(newHostPorts,hostAndPorts);
        HostAndPort hostAndPort = loadBalancer.select(newHostPorts);
        ResultWrap rw =null;
        try {
            rw=transfer.transport(mimw, hostAndPort);
            return rw;
         } catch (Exception e) {
            newHostPorts.remove(hostAndPort);
            while(newHostPorts.size()>0){
                try {
                    hostAndPort = loadBalancer.select(newHostPorts);
                    return transfer.transport(mimw, hostAndPort);
                } catch (Exception e1) {}
            }
        }
        throw new RuntimeException("已经重新尝试了"+(hostAndPorts.size()-1)+"次，宣告失败！");

    }
}
