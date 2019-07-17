package com.xxx;

import com.xxx.commons.HostAndPort;
import com.xxx.commons.MethodInvokerMetaWrap;
import com.xxx.commons.ResultWrap;

/**
 * Created by Administrator on 2018/4/8.
 */
public interface Transfer {
    public ResultWrap transport(MethodInvokerMetaWrap mimw, HostAndPort hostAndPort) throws InterruptedException;
    public void close();
}
