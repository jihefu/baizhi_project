package com.baizhi.demo;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

import java.util.Random;

/**
 * Created by Administrator on 2018/4/18.
 */
public class CustomPartitiner extends Partitioner<Text,OrderWritable> {
    public int getPartition(Text key, OrderWritable value, int numPartitions) {
        System.out.println("getPartition:"+key);
       // key.hashCode()&Integer.MAX_VALUE % numPartitions
        return new Random().nextInt(numPartitions);
    }
}
