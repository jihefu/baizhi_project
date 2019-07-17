package com.xxx.demo2;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by Administrator on 2018/4/2.
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new ObjectDecoder());//添加解码器
        pipeline.addLast(new ObjectEncoder());//添加编码器
        //消息的最终处理者
        pipeline.addLast(new ServerChannelHandlerAdapter());
    }
}
