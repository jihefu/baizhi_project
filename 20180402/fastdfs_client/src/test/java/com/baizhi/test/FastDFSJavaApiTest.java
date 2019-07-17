package com.baizhi.test;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.junit.Before;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * Created by Administrator on 2018/4/26.
 */
public class FastDFSJavaApiTest {
    StorageClient client = null;

    @Before
    public void before() throws IOException, MyException {
        ClientGlobal.init("fdfs_client.conf");
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        // client 操作分布式文件系统
        client = new StorageClient(trackerServer, null);
    }


    /**
     * 测试文件上传
     */
    @Test
    public void testUpload() throws IOException, MyException {
        String[] str = client.upload_file("E:\\1.jpg", "jpg", new NameValuePair[]{new NameValuePair("width", "1024"), new NameValuePair("author", "gaozhy")});
        for (String s : str) {
            System.out.println(s);
        }
    }

    /**
     * 测试文件下载
     */
    @Test
    public void testDownload() throws IOException, MyException {
        byte[] bytes = client.download_file("group1", "M00/00/00/wKgrhFrh5KSAWYpuAAJBWD5zXvs114.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("E:\\2.jpg");
        fileOutputStream.write(bytes);
        fileOutputStream.close();
    }

    /**
     * 获取文件的元数据
     */
    @Test
    public void testGetMetedata() throws IOException, MyException {
        NameValuePair[] nam = client.get_metadata("group1", "M00/00/00/wKgrhFrh5KSAWYpuAAJBWD5zXvs114.jpg");
        for (NameValuePair nameValuePair : nam) {
            System.out.println(nameValuePair.getName() + " | " +nameValuePair.getValue());
        }
    }

}
