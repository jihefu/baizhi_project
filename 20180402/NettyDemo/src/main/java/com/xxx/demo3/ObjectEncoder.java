package com.xxx.demo3;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/2.
 */
public class ObjectEncoder extends MessageToMessageEncoder<Object> {
    /**
     *
     * @param ctx
     * @param msg :需要序列化的数据
     * @param out ：输出的 数据帧 对象
     * @throws Exception
     */
    protected void encode(ChannelHandlerContext ctx, Object msg, List<Object> out) throws Exception {
        System.out.println("编码");
        ByteBuf buf= Unpooled.buffer();
        byte[] bytes= SerializationUtils.serialize((Serializable) msg);
        buf.writeBytes(bytes);
        out.add(buf);
    }
}
