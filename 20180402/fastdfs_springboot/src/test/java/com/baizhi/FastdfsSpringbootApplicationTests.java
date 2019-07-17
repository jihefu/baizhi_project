package com.baizhi;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FastdfsSpringbootApplicationTests {
	@Autowired
	private FastFileStorageClient fastFileStorageClient;

	@Test
	public void contextLoads() throws FileNotFoundException {
        File file = new File("E:\\1.jpg");
        FileInputStream inputStream = new FileInputStream(file);
        StorePath storePath = fastFileStorageClient.uploadFile(inputStream, file.length(), "jpg", null);
        System.out.println(storePath.getGroup() + " | "+storePath.getPath());
    }
}
