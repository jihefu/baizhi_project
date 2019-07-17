package com.xxx.demo;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/4/3.
 */
public class ZNodeCRUD {
    private ZkClient client = null;
    @Before
    public void before() {
        client=new ZkClient("192.168.100.128:2181");
    }
    @Test
    public void testCreateNode(){
        String path= client.create("/zpark", new Date(), CreateMode.PERSISTENT);
        System.out.println(path);
    }
    @Test
    public void testUpdateNodeData(){
        client.writeData("/zpark","哈哈");
    }
    @Test
    public void testReadNodeData(){
        Object value = client.readData("/zpark");
        System.out.println(value);
    }
    @Test
    public void testDeleteNodeData(){
        if(client.exists("/zpark")){
            client.deleteRecursive("/zpark");
            System.out.println("delete /zpark ...");
        }
    }
    @Test
    public void testGetChildren(){
        List<String> children = client.getChildren("/zpark/com.xxx.services.IUserService/providers");
        System.out.println(children);
    }
    @Test
    public void testCreateMany(){
        client.createPersistent("/zpark/com.xxx.services.IUserService/providers/192.168.100.128:20880",true);
        client.createPersistent("/zpark/com.xxx.services.IUserService/providers/192.168.100.128:20881",true);
    }
    @Test
    public void testSubscribeChildrenChange() throws IOException {
        client.subscribeChildChanges("/zpark/com.xxx.services.IUserService/providers", new IZkChildListener() {
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println("parentPath:"+parentPath+",currentChilds"+currentChilds);
            }
        });
        System.in.read();
    }
    @Test
    public void testSubscribeNodeDataChange() throws IOException {
        client.subscribeDataChanges("/zpark", new IZkDataListener() {
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println(dataPath+":"+data);
            }
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println(dataPath);
            }
        });
        System.in.read();
    }
    @After
    public void after() {
        client.close();//关闭 session
    }

}
