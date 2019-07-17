package com.xxx.impl;

import com.xxx.commons.HostAndPort;
import com.xxx.LoadBalancer;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2018/4/8.
 */
public class RandomLoadbalancer implements LoadBalancer {
    public HostAndPort select(List<HostAndPort> hostAndPorts) {
        int i = new Random().nextInt(hostAndPorts.size());
        return hostAndPorts.get(i);
    }
}
