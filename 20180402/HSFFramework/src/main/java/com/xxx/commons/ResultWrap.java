package com.xxx.commons;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/8.
 */
public class ResultWrap implements Serializable {
    private Result result;
    private Map<Object,Object> attchments;

    public ResultWrap(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Map<Object, Object> getAttchments() {
        return attchments;
    }

    public void setAttchments(Map<Object, Object> attchments) {
        this.attchments = attchments;
    }
}
