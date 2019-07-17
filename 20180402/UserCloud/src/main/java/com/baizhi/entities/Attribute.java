package com.baizhi.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/18.
 */
public class Attribute implements Serializable {
    private String column;
    private Object value;

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Attribute(String column, Object value) {
        this.column = column;
        this.value = value;
    }

    public Attribute() {
    }
}
