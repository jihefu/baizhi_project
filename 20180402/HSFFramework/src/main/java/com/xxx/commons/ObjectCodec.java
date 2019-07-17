package com.xxx.commons;

import com.xxx.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandlerInvoker;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.List;

/**
 * Created by Administrator on 2018/4/8.
 */
public class ObjectCodec extends MessageToMessageCodec<ByteBuf,Object> {
    private Serializer serializer;
    public ObjectCodec(Serializer serializer){
       this.serializer=serializer;
    }
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        byte[] bytes = serializer.serialize(msg);
        ByteBuf buf= Unpooled.buffer();
        buf.writeBytes(bytes);
        out.add(buf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        byte[] bytes=new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        Object obj = serializer.deserialize(bytes);
        out.add(obj);
    }
}
