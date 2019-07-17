package com.baizhi.demo;

import org.apache.hadoop.mapreduce.lib.db.DBWritable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2018/4/17.
 */
public class UserDBWritable implements DBWritable {
    private String clazz;
    private double salary;
    /**
     * 当我们使用DBOutputFormat使用
     * @param statement
     * @throws SQLException
     */
    public void write(PreparedStatement statement) throws SQLException {

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
}
