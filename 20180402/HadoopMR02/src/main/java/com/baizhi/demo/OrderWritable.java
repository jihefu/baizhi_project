package com.baizhi.demo;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by Administrator on 2018/4/16.
 */
public class OrderWritable implements Writable {
    private String name;
    private double price;
    /**
     * 序列化
     * @param out
     * @throws IOException
     */
    public void write(DataOutput out) throws IOException {
        out.writeUTF(name);
        out.writeDouble(price);
    }

    /**
     * 反序列化
     * @param in
     * @throws IOException
     */
    public void readFields(DataInput in) throws IOException {
        name=in.readUTF();
        price=in.readDouble();
    }
    public OrderWritable(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public OrderWritable() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return name + "\t" + price  ;
    }
}
