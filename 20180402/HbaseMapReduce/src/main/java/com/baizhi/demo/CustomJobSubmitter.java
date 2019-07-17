package com.baizhi.demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.nio.IntBuffer;

/**
 * Created by Administrator on 2018/4/20.
 */
public class CustomJobSubmitter {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1.创建Job
        Configuration conf= HBaseConfiguration.create();
        conf.addResource("core-site.xml");
        conf.addResource("hdfs-site.xml");
        conf.addResource("yarn-site.xml");
        conf.addResource("mapred-site.xml");

        conf.set("hbase.zookeeper.quorum","CentOS");

        conf.set("mapreduce.job.jar","file:///E:\\workspace\\20180402\\HbaseMapReduce\\target\\hbasemr-1.0-SNAPSHOT.jar");

        //开启压缩
        conf.setBoolean("mapreduce.map.output.compress", true);
        conf.setClass("mapreduce.map.output.compress.codec",  GzipCodec.class,
                CompressionCodec.class);

        Job job=Job.getInstance(conf);

        //3.设置输入和输出格式
        job.setInputFormatClass(TableInputFormat.class);
        job.setOutputFormatClass(TableOutputFormat.class);

        //4.设置读入和写出路径 表
        TableMapReduceUtil.initTableMapperJob("zpark:t_user",
                new Scan(),
                UserMapper.class,
                Text.class,
                IntWritable.class,
                job);
        TableMapReduceUtil.initTableReducerJob("zpark:t_user_res",
                UserReducer.class,job);

        //.5 设置Mapper和Reducer输出key-value 第4步已经说明
        job.setNumReduceTasks(3);//设置reduce个数

        job.waitForCompletion(true);


    }
    public static class UserMapper extends TableMapper<Text,IntWritable>{
        @Override
        protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
            String royKey= Bytes.toString(key.get());
            String sex=Bytes.toString(value.getValue("cf1".getBytes(),"sex".getBytes()));
            int age= Integer.parseInt(Bytes.toString(value.getValue("cf1".getBytes(),"age".getBytes())));
            context.write(new Text(sex),new IntWritable(age));
        }
    }



    public static class UserReducer extends TableReducer<Text,IntWritable,ImmutableBytesWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int count=0;
            float totalAge=0.0f;
            for (IntWritable value : values) {
                totalAge+=value.get();
                count++;
            }
            float aveAge=totalAge/count;
            Put put = new Put(key.getBytes());
            put.addColumn("cf1".getBytes(),"avgAge".getBytes(),(aveAge+"").getBytes());
            context.write(null,put);
        }
    }

    /*public static class UserMapper extends Mapper<ImmutableBytesWritable,Result,Text,IntWritable>{
        @Override
        protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {

        }
    }*/
}
