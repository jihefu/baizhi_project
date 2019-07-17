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
        Job job=Job.getInstance(conf);

        //2.设置数据处理格式 读入格式、写出格式
        job.setInputFormatClass(CombineTextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        //3.设置数据的处理路径 读入路径（设置多个） 输出路径（一个 且必须不存在）
        Path src=new Path("D:/demo/words");
        CombineTextInputFormat.addInputPath(job,src);

        Path dst=new Path("D:/demo/res");
        TextOutputFormat.setOutputPath(job,dst);

        FileSystem fs=FileSystem.get(conf);
        if(fs.exists(dst)){
            fs.delete(dst,true);
        }

        //4.设置数据的处理逻辑
        job.setMapperClass(WordsMapper.class);
        job.setReducerClass(WordsReducer.class);

        //5.对Mapper端和Reducer端的输出类型进行说明
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

       job.setOutputKeyClass(Text.class);
       job.setOutputValueClass(IntWritable.class);

       job.setCombinerClass(WordsCombiner.class);

        //6.提交任务
        job.waitForCompletion(true);

}
    /**
     * KEYIN: 读取一行文本数据的字节偏移量- 没有任何作用
     * VALUEIN：一行文本数据
     * KEYOUT：表示分组|归类依据 等价 group by 字段
     * VALUEOUT：需要统计的值
     */
    public static class WordsMapper extends Mapper<LongWritable,Text,Text,IntWritable>{
        /**
         * 001201801010 苹果 2 4.5 001
         * value=INFO  2017-12-10 14:14:00  com.xxx.services.UserService  192.168.0.1
         * 将 KEYOUT、VALUEOUT从value中进行剥离
         * context.write(key,value)  等价  emit(key,value);
         */
        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String[] tokens=value.toString().split(" ");
            for (String token : tokens) {
                context.write(new Text(token),new IntWritable(1));
            }
        }
    }
    public static class WordsReducer extends Reducer<Text,IntWritable,Text,Text>{
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int total=0;
            for (IntWritable value : values) {
                total+=value.get();
            }
            context.write(key,new Text(total+""));
        }
    }
    public static class WordsCombiner extends Reducer<Text,IntWritable,Text,IntWritable>{
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
