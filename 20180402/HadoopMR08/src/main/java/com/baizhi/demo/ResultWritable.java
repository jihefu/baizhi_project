package com.baizhi.demo;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by Administrator on 2018/4/18.
 */
public class ResultWritable implements Writable {
    private int uptotal;
    private int downtotal;
    private int total;

    public ResultWritable() {
    }

    public int getUptotal() {
        return uptotal;
    }

    public void setUptotal(int uptotal) {
        this.uptotal = uptotal;
    }

    public int getDowntotal() {
        return downtotal;
    }

    public void setDowntotal(int downtotal) {
        this.downtotal = downtotal;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ResultWritable(int uptotal, int downtotal, int total) {
        this.uptotal = uptotal;
        this.downtotal = downtotal;
        this.total = total;
    }

    public void write(DataOutput out) throws IOException {
        out.writeInt(uptotal);
        out.writeInt(downtotal);
        out.writeInt(total);
    }

    public void readFields(DataInput in) throws IOException {
        uptotal=in.readInt();
        downtotal=in.readInt();
        total=in.readInt();
    }

    @Override
    public String toString() {
        return "ResultWritable{" +
                "uptotal=" + uptotal +
                ", downtotal=" + downtotal +
                ", total=" + total +
                '}';
    }
}
