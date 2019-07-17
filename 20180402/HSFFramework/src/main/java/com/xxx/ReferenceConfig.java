package com.xxx;

/**
 * Created by Administrator on 2018/4/8.
 */
public class ReferenceConfig  {
    private String id;
    private Class targetInterface;
    private String cluster="failover";
    private String loadbalace="random";

    public ReferenceConfig(String id, Class targetInterface) {
        this.id = id;
        this.targetInterface = targetInterface;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Class getTargetInterface() {
        return targetInterface;
    }

    public void setTargetInterface(Class targetInterface) {
        this.targetInterface = targetInterface;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getLoadbalace() {
        return loadbalace;
    }

    public void setLoadbalace(String loadbalace) {
        this.loadbalace = loadbalace;
    }
}
