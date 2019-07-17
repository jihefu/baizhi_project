package com.baizhi.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/12.
 */
public class Token implements Serializable {
    private String name;
    private String password;
    private long lastUpdate;
    private long expire;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }
}
