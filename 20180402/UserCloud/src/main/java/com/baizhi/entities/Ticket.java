package com.baizhi.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/12.
 */
public class Ticket implements Serializable {
    private User user;
    String appId;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
