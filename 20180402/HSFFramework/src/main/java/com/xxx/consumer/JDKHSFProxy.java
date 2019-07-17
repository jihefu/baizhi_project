package com.xxx.consumer;

import com.xxx.*;
import com.xxx.commons.*;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Created by Administrator on 2018/4/8.
 */
public class JDKHSFProxy extends AbstractFactoryBean implements HSFProxy,InvocationHandler{
    private Class targetInterface;
    /*服务列表*/
    private List<HostAndPort> hostAndPorts;
    private Router router;
    private Cluster cluster;
    private Registry registry;
    private LoadBalancer loadBalancer;
    private Transfer transfer;
    public JDKHSFProxy(Class targetInterface,Registry registry){
        this.targetInterface=targetInterface;
        this.registry=registry;
        //获取服务列表
        hostAndPorts=registry.retriveService(targetInterface);
        //动态更新服务列表
        registry.subscribeService(targetInterface,hostAndPorts);
    }


    public Class<?> getObjectType() {
        return targetInterface;
    }
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //1.封装参数
        MethodInvokerMeta mim=new MethodInvokerMeta(targetInterface,method.getName(),method.getParameterTypes(),args);
        MethodInvokerMetaWrap mimw=new MethodInvokerMetaWrap(mim);
        mimw.setAttchments(RpcContext.getContext().getAllAttchments());
        //2.网络传输
          /*
          Transfer transfer=new NettyTransfer();
            ResultWrap rw = transfer.transport(mimw, new HostAndPort("192.168.8.6", 9999));
            transfer.close();
         */
        List<HostAndPort> newHostandPorts = router.route(targetInterface, hostAndPorts);
        if(newHostandPorts==null || newHostandPorts.size()==0){
            throw new RuntimeException("服务"+targetInterface.getName()+"没有可用的 Prodivers");
        }
        ResultWrap rw = cluster.invoker(newHostandPorts, loadBalancer, transfer, mimw);

        //3.结果处理
        if(rw.getResult().getRuntimeException()!=null){
            throw rw.getResult().getRuntimeException();
        }
        //4.合并服务器的附件信息和本地附件信息进行合并
        RpcContext.getContext().getAllAttchments().putAll(rw.getAttchments());

        return rw.getResult().getReturnValue();
    }

    public Router getRouter() {
        return router;
    }

    public void setRouter(Router router) {
        this.router = router;
    }

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
    public LoadBalancer getLoadBalancer() {
        return loadBalancer;
    }

    public void setLoadBalancer(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    public Transfer getTransfer() {
        return transfer;
    }

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
    }

    public Object createInstance() {
       return  Proxy.newProxyInstance(JDKHSFProxy.class.getClassLoader(),
                new Class<?>[]{targetInterface},this);
    }
}
