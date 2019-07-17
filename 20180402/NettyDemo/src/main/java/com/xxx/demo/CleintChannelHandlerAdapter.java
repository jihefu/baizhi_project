package com.xxx.demo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

/**
 * Created by Administrator on 2018/4/2.
 */
public class CleintChannelHandlerAdapter extends ChannelHandlerAdapter {
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
     * 接收响应
     * @param ctx
     * @param msg 请求
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer= (ByteBuf) msg;
        System.out.println("收到：channelRead:"+((ByteBuf) msg).toString(CharsetUtil.UTF_8));
    }
    /**
     * 发送消息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf buffer=ctx.alloc().buffer();
        buffer.writeBytes("Hello Wolrd!".getBytes());
        ctx.writeAndFlush(buffer);
    }
}
