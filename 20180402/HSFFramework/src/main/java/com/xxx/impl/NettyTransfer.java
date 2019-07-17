package com.xxx.impl;

import com.xxx.*;
import com.xxx.commons.HostAndPort;
import com.xxx.commons.MethodInvokerMetaWrap;
import com.xxx.commons.ObjectCodec;
import com.xxx.commons.ResultWrap;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * Created by Administrator on 2018/4/8.
 */
public class NettyTransfer implements Transfer {
    /*Netty相关代码*/
    private Bootstrap bt=null;
    private EventLoopGroup worker;
    public NettyTransfer(){
        bt=new Bootstrap();
        worker=new NioEventLoopGroup();
        bt.group(worker);
        bt.channel(NioSocketChannel.class);
    }
    public ResultWrap transport(final MethodInvokerMetaWrap mimw, HostAndPort hostAndPort) throws InterruptedException {

        bt.handler(new ChannelInitializer<SocketChannel>() {

            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new LengthFieldBasedFrameDecoder(65535,0,2,0,2));//数据帧解码
                pipeline.addLast(new LengthFieldPrepender(2));//数据帧编码器
                pipeline.addLast(new ObjectCodec(new JDKSerializer()));//添加解码器|编码器
                pipeline.addLast(new ChannelHandlerAdapter(){
                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        cause.printStackTrace();
                    }
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        ChannelFuture channelFuture = ctx.writeAndFlush(mimw);
                        channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                        channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                    }
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        ResultWrap resultWrap= (ResultWrap) msg;
                        mimw.setResultWrap(resultWrap);
                    }
                });
            }
        });
        ChannelFuture channelFuture = bt.connect(hostAndPort.getHost(), hostAndPort.getPort()).sync();
        channelFuture.channel().closeFuture().sync();
        return mimw.getResultWrap();
    }

    public void close() {
        worker.shutdownGracefully();
    }
}
