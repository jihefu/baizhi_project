package com.baizhi.demo;

import org.apache.hadoop.mapreduce.lib.db.DBWritable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2018/4/18.
 */
public class ResultDBWritable implements DBWritable{
    private String clazz;
    private double salary;

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

    public ResultDBWritable() {
    }

    public ResultDBWritable(String clazz, double salary) {

        this.clazz = clazz;
        this.salary = salary;
    }

    public void write(PreparedStatement statement) throws SQLException {
        statement.setString(1,clazz);
        statement.setDouble(2,salary);
    }

    public void readFields(ResultSet resultSet) throws SQLException {

    }
}
