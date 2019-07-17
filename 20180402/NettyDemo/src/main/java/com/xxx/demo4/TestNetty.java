package com.xxx.demo4;

/**
 * Created by Administrator on 2018/4/3.
 */
public class TestNetty {
    public static void main(String[] args) throws InterruptedException {
        NettyTransferClient nettyTransferClient=new NettyTransferClient();
        Object response = nettyTransferClient.call(null, null);
    }
}
