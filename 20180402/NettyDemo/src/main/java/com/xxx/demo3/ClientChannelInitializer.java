package com.xxx.demo3;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * Created by Administrator on 2018/4/2.
 */
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,0,2,0,2));//数据帧解码
        pipeline.addLast(new LengthFieldPrepender(2));//数据帧编码器
        pipeline.addLast(new ObjectDecoder());//添加解码器
        pipeline.addLast(new ObjectEncoder());//添加编码器

        //消息的最终处理者
        pipeline.addLast(new CleintChannelHandlerAdapter());
    }
}
