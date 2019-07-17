package com.xxx.demo2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by Administrator on 2018/4/2.
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        //1.创建启动引导
        Bootstrap bt=new Bootstrap();
        //2.创建boss-请求转发、worker-响应处理线程池组
        EventLoopGroup work=new NioEventLoopGroup();
        //3.设置线程池组 boss、worker
        bt.group(work);
        //4.配置Channel底层实现了类
        bt.channel(NioSocketChannel.class);
       //5.初始化netty通讯管道 - 重点
        bt.handler(new ClientChannelInitializer());
        //6.绑定服务端口，启动服务
        ChannelFuture channelFuture = bt.connect("127.0.0.1",9999).sync();
        //7.关闭服务通道
        channelFuture.channel().closeFuture().sync();
        //8.释放线程组资源
        work.shutdownGracefully();
    }
}
