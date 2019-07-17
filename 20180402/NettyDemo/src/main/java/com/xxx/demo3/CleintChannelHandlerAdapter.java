package com.xxx.demo3;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

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
        //ByteBuf buffer= (ByteBuf) msg;
        System.out.println("收到：channelRead:"+msg);
    }
    /**
     * 发送消息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

       for(int i=0;i<1000;i++){
           ChannelFuture channelFuture = ctx.writeAndFlush(new Date());

           channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
           channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
       }

    }
}
