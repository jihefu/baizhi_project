package com.baizhi.demo;

import org.apache.curator.framework.api.Watchable;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2018/4/17.
 */
public class UserDBWritable implements DBWritable /*,Writable*/ {
    private String clazz;
    private double salary;

    public UserDBWritable(String clazz, double salary) {
        this.clazz = clazz;
        this.salary = salary;
    }

    public UserDBWritable() {
    }

    /**
     * 当我们使用DBOutputFormat使用
     * @param statement
     * @throws SQLException
     */

    public void write(PreparedStatement statement) throws SQLException {
        statement.setString(1,clazz);
        statement.setDouble(2,salary);
    }

    /**
     * 使用DBInputFormat时候使用
     * @param resultSet
     * @throws SQLException
     */
    public void readFields(ResultSet resultSet) throws SQLException {
            clazz=resultSet.getString("class");
            salary=resultSet.getDouble("salary");
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    /*public void write(DataOutput out) throws IOException {
        out.writeUTF(this.clazz);
        out.writeDouble(salary);
    }

    public void readFields(DataInput in) throws IOException {
        clazz = in.readUTF();
        salary = in.readDouble();
    }*/
}
