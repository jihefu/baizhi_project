package com.xxx.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by Administrator on 2018/4/2.
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        //1.创建服务启动引导
        ServerBootstrap sbt=new ServerBootstrap();
        //2.创建boss-请求转发、worker-响应处理线程池组
        EventLoopGroup boss=new NioEventLoopGroup();
        EventLoopGroup work=new NioEventLoopGroup();
        //3.设置线程池组 boss、worker
        sbt.group(boss,work);
        //4.配置Channel底层实现了类
        sbt.channel(NioServerSocketChannel.class);
       //5.初始化netty通讯管道 - 重点
        sbt.childHandler(new ServerChannelInitializer());
        //6.绑定服务端口，启动服务
        System.out.println("我在9999监听...");
        ChannelFuture channelFuture = sbt.bind(9999).sync();
        //7.关闭服务通道
        channelFuture.channel().closeFuture().sync();
        //8.释放线程组资源
        boss.shutdownGracefully();
        work.shutdownGracefully();
    }
}
