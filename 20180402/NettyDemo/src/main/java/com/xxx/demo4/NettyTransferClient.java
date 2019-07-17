package com.xxx.demo4;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2018/4/3.
 */
public class NettyTransferClient {
    private static Bootstrap bt;
    private static EventLoopGroup worker;

    public NettyTransferClient() {
        bt=new Bootstrap();
        worker=new NioEventLoopGroup();
        bt.group(worker);
        bt.channel(NioSocketChannel.class);
    }
    public Object call(Object request,HostAndPort hostAndPort) throws InterruptedException {
        bt.handler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel ch) throws Exception {

            }
        });
        ChannelFuture channelFuture = bt.connect(new InetSocketAddress(hostAndPort.getHost(), hostAndPort.getPort())).sync();
        channelFuture.channel().closeFuture().sync();
        return null;
    }
    public void close(){
        worker.shutdownGracefully();
    }
}
