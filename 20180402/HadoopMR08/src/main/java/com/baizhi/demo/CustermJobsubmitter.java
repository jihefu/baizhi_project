package com.baizhi.demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
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
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        //3.设置数据的处理路径 读入路径（设置多个） 输出路径（一个 且必须不存在）
        Path src=new Path("D:/demo/fllow");
        TextInputFormat.addInputPath(job,src);

        Path dst=new Path("D:/demo/res");
        TextOutputFormat.setOutputPath(job,dst);

        FileSystem fs=FileSystem.get(conf);
        if(fs.exists(dst)){
            fs.delete(dst,true);
        }

        //4.设置数据的处理逻辑
        job.setMapperClass(FllowMapper.class);
        job.setReducerClass(OrderReducer.class);

        //5.对Mapper端和Reducer端的输出类型进行说明
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FllowWritable.class);

       job.setOutputKeyClass(Text.class);
       job.setOutputValueClass(ResultWritable.class);


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
    public static class FllowMapper extends Mapper<LongWritable,Text,Text,FllowWritable>{
        /**
         * 15652034181 1024 2048 2016-10-10
         * 将 KEYOUT、VALUEOUT从value中进行剥离
         * context.write(key,value)  等价  emit(key,value);
         */
        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String[] tokens=value.toString().split(" ");
            String phone=tokens[0];
            FllowWritable fllowWritable = new FllowWritable(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
            context.write(new Text(phone),fllowWritable);
        }
    }
    public static class OrderReducer extends Reducer<Text,FllowWritable,Text,ResultWritable>{
        private MultipleOutputs<Text,ResultWritable> outputs;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            outputs=new MultipleOutputs<Text, ResultWritable>(context);
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            outputs.close();
        }

        @Override
        protected void reduce(Text key, Iterable<FllowWritable> values, Context context)
                throws IOException, InterruptedException {
             int totalUp=0,totalDown=0,total=0;
            for (FllowWritable value : values) {
                totalUp+=value.getUpfllow();
                totalDown+=value.getDownfllow();
            }
            total=totalDown+totalUp;
            ResultWritable resultWritable = new ResultWritable(totalDown, totalUp, total);
            String phone=key.toString();
            String basepath=phone.substring(0,3)+"/"+phone.substring(3,6);
           // context.write(key,resultWritable);
            outputs.write(key,resultWritable,basepath);
        }
    }
}
