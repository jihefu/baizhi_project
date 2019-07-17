package com.xxx.commons;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/8.
 */
public class Result implements Serializable {
    private Object returnValue;
    private RuntimeException runtimeException;

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public RuntimeException getRuntimeException() {
        return runtimeException;
    }

    public void setRuntimeException(RuntimeException runtimeException) {
        this.runtimeException = runtimeException;
    }
}
