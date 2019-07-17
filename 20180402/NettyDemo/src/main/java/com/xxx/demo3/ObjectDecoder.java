package com.xxx.demo3;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.apache.commons.lang3.SerializationUtils;

import java.util.List;

/**
 * Created by Administrator on 2018/4/2.
 */
public class ObjectDecoder extends MessageToMessageDecoder<ByteBuf> {
    /**
     *
     * @param ctx
     * @param msg
     * @param out :解码数据帧
     * @throws Exception
     */
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        System.out.println("解码");
        byte[] bytes=new byte[msg.readableBytes()];
        msg.readBytes(bytes);
        Object value = SerializationUtils.deserialize(bytes);
        out.add(value);
    }
}
