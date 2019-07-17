package com.xxx;

import com.xxx.commons.HostAndPort;

import java.util.List;

/**
 * Created by Administrator on 2018/4/8.
 */
public interface LoadBalancer {
    public HostAndPort select(List<HostAndPort> hostAndPorts);
}
