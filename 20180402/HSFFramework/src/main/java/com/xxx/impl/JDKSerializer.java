package com.xxx.impl;

import com.xxx.Serializer;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/8.
 */
public class JDKSerializer implements Serializer {
    public byte[] serialize(Object object) {
        return SerializationUtils.serialize((Serializable) object);
    }

    public Object deserialize(byte[] bytes) {
        return SerializationUtils.deserialize(bytes);
    }
}
