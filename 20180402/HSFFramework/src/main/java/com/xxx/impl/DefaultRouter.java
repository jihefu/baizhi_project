package com.xxx.impl;

import com.xxx.commons.HostAndPort;
import com.xxx.Router;

import java.util.List;

/**
 * Created by Administrator on 2018/4/8.
 */
public class DefaultRouter implements Router {
    public List<HostAndPort> route(Class targetInterface, List<HostAndPort> hostAndPorts) {
        //查询路由规则对请求过滤
        String routesPath = PREFIX + "/" + targetInterface.getName() + SUFFIX;
        return hostAndPorts;
    }
}
