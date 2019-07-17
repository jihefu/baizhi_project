package com.baizhi.demo;


import java.sql.*;

/**
 * Created by Administrator on 2018/4/25.
 */
public class JDBCTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        //1.加载驱动
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        //2.获取连接
        Connection conn = DriverManager.getConnection("jdbc:hive2://CentOS:10000/baizhi", "root", "");


        PreparedStatement ps1 = conn.prepareStatement("ADD JAR /usr/apache-hive-1.2.1-bin/hcatalog/share/hcatalog/hive-hcatalog-core-1.2.1.jar");
        ps1.execute();

        //3.创建PreparedStatement
        PreparedStatement ps = conn.prepareStatement("select id,name,sex,birth from t_user_json where id=?");
       // ps.setInt(1,1);

        //4.执行statement
        ResultSet rs = ps.executeQuery();
        //5.遍历结果集
        while (rs.next()){
            int id = rs.getInt("id");
            String name = rs.getString("name");
            boolean sex = rs.getBoolean("sex");
            Date birth = rs.getDate("birth");
            System.out.println(id+" "+name+" "+sex+" "+birth);
        }
        //6.关闭conn、ps、rs
        rs.close();
        ps1.close();
        ps.close();
        conn.close();
    }
}
