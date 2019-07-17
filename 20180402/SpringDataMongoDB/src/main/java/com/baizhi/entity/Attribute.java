package com.baizhi.entity;

import org.springframework.data.annotation.PersistenceConstructor;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/10.
 */
public class Attribute implements Serializable {
    private String columnName;
    private Object value;

    @PersistenceConstructor
    public Attribute(String columnName, Object value) {
        this.columnName = columnName;
        this.value = value;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
