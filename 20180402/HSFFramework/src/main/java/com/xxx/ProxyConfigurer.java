package com.xxx;

import com.xxx.consumer.JDKHSFProxy;
import com.xxx.impl.DefaultRouter;
import com.xxx.impl.FailoverCluster;
import com.xxx.impl.NettyTransfer;
import com.xxx.impl.RandomLoadbalancer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/8.
 */
public class ProxyConfigurer implements BeanFactoryPostProcessor {
    private Registry registry;
    private List<ReferenceConfig> refereces;

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public List<ReferenceConfig> getRefereces() {
        return refereces;
    }

    public void setRefereces(List<ReferenceConfig> refereces) {
        this.refereces = refereces;
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        DefaultListableBeanFactory dlbf= (DefaultListableBeanFactory) configurableListableBeanFactory;
        /*
        <bean id="failoverCluster" class="com.xxx.impl.FailoverCluster"/>
        <bean id="randomLoadbalancer" class="com.xxx.impl.RandomLoadbalancer"/>
        <bean id="defaultRouter" class="com.xxx.impl.DefaultRouter"/>
        <bean id="nettyTransfer" class="com.xxx.impl.NettyTransfer"/>
        */
        BeanDefinitionBuilder failover=BeanDefinitionBuilder.genericBeanDefinition(FailoverCluster.class);
        dlbf.registerBeanDefinition("failoverCluster",failover.getBeanDefinition());

        BeanDefinitionBuilder loadbalance=BeanDefinitionBuilder.genericBeanDefinition(RandomLoadbalancer.class);
        dlbf.registerBeanDefinition("randomLoadbalancer",loadbalance.getBeanDefinition());

        BeanDefinitionBuilder router=BeanDefinitionBuilder.genericBeanDefinition(DefaultRouter.class);
        dlbf.registerBeanDefinition("defaultRouter",router.getBeanDefinition());

        BeanDefinitionBuilder nettyTransfer=BeanDefinitionBuilder.genericBeanDefinition(NettyTransfer.class);
        dlbf.registerBeanDefinition("nettyTransfer",nettyTransfer.getBeanDefinition());

        /*
        <bean id="demoService" class="com.xxx.consumer.JDKHSFProxy"  >
                <constructor-arg name="registry" ref="zookeeperReigstry"/>
                <constructor-arg name="targetInterface" value="com.baizhi.IDemoService"/>
                <property name="cluster" ref="failoverCluster"/>
                <property name="loadBalancer" ref="randomLoadbalancer"/>
                <property name="router" ref="defaultRouter"/>
                <property name="transfer" ref="nettyTransfer"/>
        </bean>
        */
        for (ReferenceConfig referece : refereces) {
            BeanDefinitionBuilder proxyBean=BeanDefinitionBuilder.genericBeanDefinition(JDKHSFProxy.class);
            proxyBean.addConstructorArgValue(referece.getTargetInterface());
            proxyBean.addConstructorArgValue(registry);

            proxyBean.addPropertyReference("cluster","failoverCluster");
            proxyBean.addPropertyReference("loadBalancer","randomLoadbalancer");
            proxyBean.addPropertyReference("router","defaultRouter");
            proxyBean.addPropertyReference("transfer","nettyTransfer");

            dlbf.registerBeanDefinition(referece.getId(),proxyBean.getBeanDefinition());
        }


    }
}
