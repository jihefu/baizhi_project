package com.baizhi.demo1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.junit.Test;

import java.io.*;

/**
 * Created by Administrator on 2018/3/13.
 */
public class HDFSDemo {
    @Test
    public void testMkdir() throws IOException {
        Configuration conf=new Configuration();
        conf.addResource("core-site.xml");

        FileSystem fileSystem=FileSystem.get(conf);

        Path dir=new Path("/baizhi");
        if(fileSystem.exists(dir)){
            System.out.println("dir already exists ...");
        }else{
            fileSystem.mkdirs(dir);
        }
    }
    @Test
    public void testUpload01() throws IOException {
        Configuration conf=new Configuration();
        conf.addResource("core-site.xml");
        FileSystem fileSystem=FileSystem.get(conf);

        OutputStream os=fileSystem.create(new Path("/a.xml"));
        InputStream is=new FileInputStream("C:\\Users\\Administrator\\Desktop\\core-site.xml");

        IOUtils.copyBytes(is,os,1024,true);

    }
    @Test
    public void testUpload02() throws IOException {
        Configuration conf=new Configuration();
        conf.addResource("core-site.xml");
        FileSystem fileSystem=FileSystem.get(conf);

        Path src = new Path("C:\\Users\\Administrator\\Desktop\\core-site.xml");
        Path dst = new Path("/b.xml");

        fileSystem.copyFromLocalFile(src,dst);
    }
    @Test
    public void testDownload01() throws IOException {
        Configuration conf=new Configuration();
        conf.addResource("core-site.xml");
        FileSystem fileSystem=FileSystem.get(conf);

        InputStream is=fileSystem.open(new Path("/a.xml"));
        OutputStream os=new FileOutputStream("C:\\Users\\Administrator\\Desktop\\core-site1.xml");

        IOUtils.copyBytes(is,os,1024,true);
    }
    @Test
    public void testDownload02() throws IOException {
        Configuration conf=new Configuration();
        conf.addResource("core-site.xml");
        FileSystem fileSystem=FileSystem.get(conf);

        Path dst = new Path("C:\\Users\\Administrator\\Desktop\\core-site3.xml");
        Path src = new Path("/b.xml");

        //fileSystem.copyToLocalFile(src,dst);
        fileSystem.copyToLocalFile(false,src,dst,true);

    }
    @Test
    public void testDelete() throws IOException {
        Configuration conf=new Configuration();
        conf.addResource("core-site.xml");
        FileSystem fileSystem=FileSystem.get(conf);
        Path src = new Path("/b.xml");
        fileSystem.delete(src,true);

    }
    @Test
    public void testListFiles() throws IOException {
        Configuration conf=new Configuration();
        conf.addResource("core-site.xml");
        FileSystem fileSystem=FileSystem.get(conf);

        Path path = new Path("/");
        RemoteIterator<LocatedFileStatus> listFiles = fileSystem.listFiles(path, true);
        while(listFiles.hasNext()){
            LocatedFileStatus f = listFiles.next();
            System.out.println(f.getPath()+" "+f.isDirectory());
        }
    }
    @Test
    public void testListFileStatus() throws IOException {
        Configuration conf=new Configuration();
        conf.addResource("core-site.xml");
        FileSystem fileSystem=FileSystem.get(conf);

        FileStatus[] listStatus = fileSystem.listStatus(new Path("/"));
        for (FileStatus status : listStatus) {
            System.out.println(status.getPath()+" "+status.isDirectory());
        }
    }
    @Test
    public void testCreateSequenceFile() throws IOException {
        Configuration conf=new Configuration();
        conf.addResource("core-site.xml");

        SequenceFile.Writer writer = SequenceFile.createWriter(conf,
                SequenceFile.Writer.file(new Path("/aa.seq")),
                SequenceFile.Writer.keyClass(Text.class),
                SequenceFile.Writer.valueClass(IntWritable.class)
        );

        for (int i=0;i<100;i++){
            writer.append(new Text("key"+i),new IntWritable(i));
        }

        writer.close();
    }
    @Test
    public void testReadSequenceFile() throws IOException {
        Configuration conf=new Configuration();
        conf.addResource("core-site.xml");

        SequenceFile.Reader reader =new  SequenceFile.Reader(conf,
                SequenceFile.Reader.file(new Path("/aa.seq")));

        Text key = new Text();
        IntWritable value=new IntWritable();
        while (reader.next(key,value)){
            System.out.println(key.toString()+" : "+value.get());
        }
        reader.close();
    }
}
