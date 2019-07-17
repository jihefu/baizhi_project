package com.baizhi.demo;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by Administrator on 2018/4/18.
 */
public class FllowWritable implements Writable {
    private int upfllow;
    private int downfllow;

    public FllowWritable() {
    }

    public FllowWritable(int upfllow, int downfllow) {
        this.upfllow = upfllow;
        this.downfllow = downfllow;
    }

    public int getUpfllow() {
        return upfllow;
    }

    public void setUpfllow(int upfllow) {
        this.upfllow = upfllow;
    }

    public int getDownfllow() {
        return downfllow;
    }

    public void setDownfllow(int downfllow) {
        this.downfllow = downfllow;
    }

    public void write(DataOutput out) throws IOException {
        out.writeInt(upfllow);
        out.writeInt(downfllow);
    }

    public void readFields(DataInput in) throws IOException {
        upfllow=in.readInt();
        downfllow=in.readInt();
    }
}
