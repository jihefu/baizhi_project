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
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2018/4/16.
 *
 */
public class CustermJobsubmitter {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1.创建Job对象
        Configuration conf=new Configuration();
        Job job=Job.getInstance(conf);

        //2.设置数据处理格式 读入格式、写出格式
        job.setOutputFormatClass(TextOutputFormat.class);

        //3.设置数据的处理路径 读入路径（设置多个） 输出路径（一个 且必须不存在）

        Path dst=new Path("D:/demo/res");
        TextOutputFormat.setOutputPath(job,dst);

        FileSystem fs=FileSystem.get(conf);
        if(fs.exists(dst)){
            fs.delete(dst,true);
        }

        //4.设置数据的处理逻辑
        MultipleInputs.addInputPath(job,new Path("D:/demo/multi/o1"),TextInputFormat.class,OrderMapper1.class);
        MultipleInputs.addInputPath(job,new Path("D:/demo/multi/o2"),TextInputFormat.class,OrderMapper2.class);

        job.setReducerClass(OrderReducer.class);


        //5.对Mapper端和Reducer端的输出类型进行说明
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(OrderWritable.class);

       job.setOutputKeyClass(Text.class);
       job.setOutputValueClass(Text.class);


        //6.提交任务
        job.waitForCompletion(true);

}
    /**
     * KEYIN: 读取一行文本数据的字节偏移量- 没有任何作用
     * VALUEIN：一行文本数据
     * KEYOUT：表示分组|归类依据 等价 group by 字段
     * VALUEOUT：需要统计的值
     */
    public static class OrderMapper1 extends Mapper<LongWritable,Text,Text,OrderWritable>{
        /**
         * 001201801010 苹果 2 4.5 001
         * 将 KEYOUT、VALUEOUT从value中进行剥离
         * context.write(key,value)  等价  emit(key,value);
         */
        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String[] tokens=value.toString().split(" ");
            String userID=tokens[4];
            String name=tokens[1];
            Double price=Integer.parseInt(tokens[2])*Double.parseDouble(tokens[3]);
            context.write(new Text(userID),new OrderWritable(name,price));
        }
    }
    /**
     * KEYIN: 读取一行文本数据的字节偏移量- 没有任何作用
     * VALUEIN：一行文本数据
     * KEYOUT：表示分组|归类依据 等价 group by 字段
     * VALUEOUT：需要统计的值
     */
    public static class OrderMapper2 extends Mapper<LongWritable,Text,Text,OrderWritable>{
        /**
         * 001201701010 2 香蕉 3.5 001
         * 将 KEYOUT、VALUEOUT从value中进行剥离
         * context.write(key,value)  等价  emit(key,value);
         */
        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String[] tokens=value.toString().split(" ");
            String userID=tokens[4];
            String name=tokens[2];
            Double price=Integer.parseInt(tokens[1])*Double.parseDouble(tokens[3]);
            context.write(new Text(userID),new OrderWritable(name,price));
        }
    }
    public static class OrderReducer extends Reducer<Text,OrderWritable,Text,Text>{
        @Override
        protected void reduce(Text key, Iterable<OrderWritable> values, Context context)
                throws IOException, InterruptedException {
            double totalCost=0.0;
            Set<String> iterms=new HashSet<String>();
            for (OrderWritable value : values) {
                iterms.add(value.getName());
                totalCost+=value.getPrice();
            }
            context.write(key,new Text(iterms.toString()+" "+totalCost));
        }
    }
}
