package com.baizhi.demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

/**
 * Created by Administrator on 2018/4/16.
 *
 */
public class CustermJobsubmitter {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1.创建Job对象
        Configuration conf=new Configuration();

        conf.addResource("core-site.xml");
        conf.addResource("hdfs-site.xml");
        conf.addResource("yarn-site.xml");
        conf.addResource("mapred-site.xml");

        conf.set("mapreduce.job.jar","file:///E:\\workspace\\20180402\\HadoopMR03\\target\\mr03-1.0-SNAPSHOT.jar");

        Job job=Job.getInstance(conf);


        //2.设置数据处理格式 读入格式、写出格式
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        //3.设置数据的处理路径 读入路径（设置多个） 输出路径（一个 且必须不存在）
        Path src=new Path("/demo/src");
        TextInputFormat.addInputPath(job,src);

        Path dst=new Path("/demo/res");
        TextOutputFormat.setOutputPath(job,dst);

        FileSystem fs=FileSystem.get(conf);
        if(fs.exists(dst)){
                fs.delete(dst,true);
        }

        //4.设置数据的处理逻辑
        job.setMapperClass(IPMapper.class);
        job.setReducerClass(IPReducer.class);

        //5.对Mapper端和Reducer端的输出类型进行说明
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        //6.提交任务
        //job.submit();
        job.waitForCompletion(true);

    }
    /**
     * KEYIN: 读取一行文本数据的字节偏移量- 没有任何作用
     * VALUEIN：一行文本数据
     * KEYOUT：表示分组|归类依据 等价 group by 字段
     * VALUEOUT：需要统计的值
     */
    public static class IPMapper extends Mapper<LongWritable,Text,Text,IntWritable>{
        /**
         * value=INFO  2017-12-10 14:14:00  com.xxx.services.UserService  192.168.0.1
         * 将 KEYOUT、VALUEOUT从value中进行剥离
         * context.write(key,value)  等价  emit(key,value);
         */
        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String[] tokens=value.toString().split(" ");
            Text k=new Text(tokens[4]);
            IntWritable v=new IntWritable(1);
            context.write(k,v);
        }
    }
    public static class IPReducer extends Reducer<Text,IntWritable,Text,IntWritable>{
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int total=0;
            for (IntWritable value : values) {
                total+=value.get();
            }
            context.write(key,new IntWritable(total));
        }
    }
}
