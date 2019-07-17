package com.xxx;

/**
 * Created by Administrator on 2018/4/8.
 */
public interface Serializer {
    public byte[] serialize(Object object);
    public Object deserialize(byte[] bytes);
}
