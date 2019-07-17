package com.xxx.demo4;

/**
 * Created by Administrator on 2018/4/3.
 */
public class HostAndPort {
    private String host;
    private int port;

    public HostAndPort(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
