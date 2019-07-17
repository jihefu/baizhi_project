package com.baizhi.demo;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBInputFormat;
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
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

        DBConfiguration.configureDB(conf,
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://127.0.0.1:3306/baizhi",
                "root","root");
        conf.setInt("mapreduce.job.maps",2);
        Job job=Job.getInstance(conf);

        //2.设置数据处理格式 读入格式、写出格式
        job.setInputFormatClass(DBInputFormat.class);

        //3.设置数据的处理路径 读入路径（设置多个） 输出路径（一个 且必须不存在）
        String inputQuery="select class,salary from t_user";
        String countQuery="select count(id) from t_user";
        DBInputFormat.setInput(job,UserDBWritable.class,inputQuery,countQuery);

        DBOutputFormat.setOutput(job,"t_salary","clazz","salary");
        TextOutputFormat.setOutputPath(job,new Path("D:/demo/res"));

        MultipleOutputs.addNamedOutput(job,"file",TextOutputFormat.class,Text.class,DoubleWritable.class);
        MultipleOutputs.addNamedOutput(job,"db",DBOutputFormat.class,ResultDBWritable.class, DBInputFormat.NullDBWritable.class);


        //4.设置数据的处理逻辑
        job.setMapperClass(OrderMapper.class);
        job.setReducerClass(OrderReducer.class);

        //5.对Mapper端和Reducer端的输出类型进行说明
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(DoubleWritable.class);

       job.setOutputKeyClass(Text.class);
       job.setOutputValueClass(DoubleWritable.class);

        //6.提交任务
        job.waitForCompletion(true);

}

    public static class OrderMapper extends Mapper<LongWritable,UserDBWritable,Text,DoubleWritable>{
        /**
         * value=INFO  2017-12-10 14:14:00  com.xxx.services.UserService  192.168.0.1
         * 将 KEYOUT、VALUEOUT从value中进行剥离
         * context.write(key,value)  等价  emit(key,value);
         */
        @Override
        protected void map(LongWritable key, UserDBWritable value, Context context)
                throws IOException, InterruptedException {
            context.write(new Text(value.getClazz()),new DoubleWritable(value.getSalary()));
        }
    }
    public static class OrderReducer extends Reducer<Text,DoubleWritable,Text,DoubleWritable>{
        private MultipleOutputs outputs;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            outputs=new MultipleOutputs(context);
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            outputs.close();
        }

        @Override
        protected void reduce(Text key, Iterable<DoubleWritable> values, Context context)
                throws IOException, InterruptedException {
            double totalCost=0.0;
            int count=0;
            for (DoubleWritable value : values) {
                totalCost+=value.get();
                count++;
            }
            //context.write(key,new DoubleWritable(totalCost/count));
            outputs.write("file",key,new DoubleWritable(totalCost/count));
            outputs.write("db",new ResultDBWritable(key.toString(),totalCost/count),new DBInputFormat.NullDBWritable());
        }
    }
}
