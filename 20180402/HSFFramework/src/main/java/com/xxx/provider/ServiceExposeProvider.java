package com.xxx.provider;

import com.xxx.*;
import com.xxx.commons.*;
import com.xxx.impl.JDKSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/8.
 */
public class ServiceExposeProvider {
    /*需要暴露的服务*/
    private Map<Class,Object> exposeBeanMap;
    /*注册服务*/
    private Registry registry;
    /*netty相关参数*/
    ServerBootstrap sbt;
    EventLoopGroup boss;
    EventLoopGroup worker;
    int port;
    public  ServiceExposeProvider(int port){
        this.port=port;

        sbt=new ServerBootstrap();
        boss=new NioEventLoopGroup();
        worker=new NioEventLoopGroup();
        sbt.group(boss,worker);
        sbt.channel(NioServerSocketChannel.class);
    }
    public void start() throws UnknownHostException {
        /*接收参数、处理请求*/
        sbt.childHandler(new ChannelInitializer<SocketChannel>() {
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
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        MethodInvokerMetaWrap mimw= (MethodInvokerMetaWrap) msg;

                        //获取发射调用必要参数
                        MethodInvokerMeta invokerMeta = mimw.getInvokerMeta();
                        //获取附件信息
                        Map<Object, Object> attchments = mimw.getAttchments();
                        //将附件信息放置到当前调用的上下文信息
                        for(Map.Entry<Object,Object> entry : attchments.entrySet()) {
                            RpcContext.getContext().setAttchment(entry.getKey(),entry.getValue());
                        }

                        /*反射调用本地实现类的方法*/
                        Object targetObject=exposeBeanMap.get(invokerMeta.getTargetInterface());
                        Method method=targetObject.getClass().getDeclaredMethod(invokerMeta.getMethodName(),invokerMeta.getParameterTypes());
                        if(!method.isAccessible()){
                            method.setAccessible(true);
                        }
                        //封装结果
                        Result result=new Result();
                        try {
                            Object returnValue = method.invoke(targetObject, invokerMeta.getArgs());
                            result.setReturnValue(returnValue);
                        } catch (Exception e) {
                            result.setRuntimeException(new RuntimeException(e.getCause()));
                        }
                        //封装ResultWrap
                        ResultWrap rw=new ResultWrap(result);
                        //将服务器端的附件信息发送给客户端
                        rw.setAttchments(RpcContext.getContext().getAllAttchments());

                        //将结果写会客户端
                        ChannelFuture channelFuture = ctx.writeAndFlush(rw);
                        channelFuture.addListener(ChannelFutureListener.CLOSE);
                        channelFuture.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                        channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                    }
                });
            }
        });
        /*为了避免阻塞Spring容器初始化，需要将服务的启动做为异步*/
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("服务在@"+port+"启动...");
                    ChannelFuture channelFuture = sbt.bind(port).sync();
                    channelFuture.channel().closeFuture().sync();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        /*注册服务到注册中心*/
        HostAndPort hostAndPort=new HostAndPort(InetAddress.getLocalHost().getHostAddress(),port);
        for(Class targetInterface : exposeBeanMap.keySet()){
            registry.register(targetInterface,hostAndPort);
        }
        /*启动服务器*/
        //将启动netty线程设置为守护线程
        thread.setDaemon(true);
        thread.start();
    }
    public void close(){
        registry.close();
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }

    public void setExposeBeanMap(Map<Class, Object> exposeBeanMap) {
        this.exposeBeanMap = exposeBeanMap;
    }
    public void setRegistry(Registry registry) {
        this.registry = registry;
    }
}
