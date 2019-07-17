package com.xxx;

import com.xxx.commons.HostAndPort;

import java.util.List;

/**
 * /rpc/接口名/routers/路由规则表达式
 */
public interface Router {
    String PREFIX="/rpc";
    String SUFFIX="/routers";
    public List<HostAndPort>  route(Class targetInterface, List<HostAndPort> hostAndPorts);
}
