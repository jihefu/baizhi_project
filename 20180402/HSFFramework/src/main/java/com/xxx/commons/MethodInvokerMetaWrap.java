package com.xxx.commons;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/8.
 */
public class MethodInvokerMetaWrap implements Serializable {
    private MethodInvokerMeta invokerMeta;
    private Map<Object,Object> attchments;
    //该属性不参与序列化
    private transient ResultWrap resultWrap;
    @Override
    public String toString() {
        return "MethodInvokerMetaWrap{" +
                "invokerMeta=" + invokerMeta +
                ", attchments=" + attchments +
                '}';
    }

    public MethodInvokerMetaWrap(MethodInvokerMeta invokerMeta) {
        this.invokerMeta = invokerMeta;
    }

    public MethodInvokerMeta getInvokerMeta() {
        return invokerMeta;
    }

    public void setInvokerMeta(MethodInvokerMeta invokerMeta) {
        this.invokerMeta = invokerMeta;
    }

    public Map<Object, Object> getAttchments() {
        return attchments;
    }

    public void setAttchments(Map<Object, Object> attchments) {
        this.attchments = attchments;
    }

    public ResultWrap getResultWrap() {
        return resultWrap;
    }

    public void setResultWrap(ResultWrap resultWrap) {
        this.resultWrap = resultWrap;
    }
}
