package com.xxx.commons;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Administrator on 2018/4/8.
 */
public class MethodInvokerMeta implements Serializable {
    private Class targetInterface;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] args;

    @Override
    public String toString() {
        return "MethodInvokerMeta{" +
                "targetInterface=" + targetInterface +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", args=" + Arrays.toString(args) +
                '}';
    }

    public MethodInvokerMeta(Class targetInterface, String methodName, Class<?>[] parameterTypes, Object[] args) {
        this.targetInterface = targetInterface;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }

    public Class getTargetInterface() {
        return targetInterface;
    }

    public void setTargetInterface(Class targetInterface) {
        this.targetInterface = targetInterface;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
