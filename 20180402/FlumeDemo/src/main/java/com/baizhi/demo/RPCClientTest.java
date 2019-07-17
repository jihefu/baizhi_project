package com.baizhi.demo;

import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Administrator on 2018/4/25.
 */
public class RPCClientTest {
    public static void main(String[] args) throws EventDeliveryException {
        Properties props = new Properties();
        props.put("client.type", "default_failover");
        props.put("hosts", "h1 h2");
        String host1 = "CentOS:44444";
        String host2 = "CentOS:44444";
        props.put("hosts.h1", host1);
        props.put("hosts.h2", host2);
        props.put("host-selector", "random");
        Event event = EventBuilder.withBody("this is my data1", Charset.forName("UTF-8"));
        event.getHeaders().put("user","zhangsan");
        event.getHeaders().put("time",System.currentTimeMillis()+"");
        RpcClient rpcClient = RpcClientFactory.getInstance(props);
        rpcClient.append(event);
        rpcClient.close();
        System.out.println("&&&&&&&&&&&&&&&&&");

    }
    public void testOneNode() throws EventDeliveryException {
        Event event = EventBuilder.withBody("this is my data", Charset.forName("UTF-8"));
        event.getHeaders().put("user","zhangsan");
        event.getHeaders().put("time",System.currentTimeMillis()+"");
        RpcClient rpcClient = RpcClientFactory.getDefaultInstance("CentOS", 44444);
        rpcClient.append(event);
        rpcClient.close();
        System.out.println("----------");
    }
}
