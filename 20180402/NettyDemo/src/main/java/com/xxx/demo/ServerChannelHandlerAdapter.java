package com.xxx.demo;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by Administrator on 2018/4/2.
 */
public class ServerChannelHandlerAdapter extends ChannelHandlerAdapter {
    /**
     * 捕获netty异常信息
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        System.err.println("错误:"+cause.getMessage());
    }
    /**
     * 处理请求响应
     * @param ctx
     * @param msg 请求
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead:"+msg);
        ChannelFuture channelFuture = ctx.writeAndFlush(msg);
        //关闭socket
        channelFuture.addListener(ChannelFutureListener.CLOSE);
    }
}
